package com.notification.service.impl;

import com.notification.document.InvitationDetails;
import com.notification.exception.BadRequestException;
import com.notification.exception.RecordNotFoundException;
import com.notification.helper.EmailSenderHelper;
import com.notification.model.ResponseModel;
import com.notification.model.Status;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.InvitationDetailResponse;
import com.notification.model.response.InvitationStatusResponse;
import com.notification.model.response.InviteResponse;
import com.notification.repo.InvitationDetailsRepo;
import com.notification.service.NotificationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

import static com.notification.constant.NotificationConstant.*;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private InvitationDetailsRepo invitationDetailsRepo;

	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private EmailSenderHelper emailSenderHelper;

	@Value("${spring.mail.username}")
	private String fromMail;

	@Value("${application.ui.base-url}")
	private String domainUrl;

	@Value("${application.auth.base-url}")
	private String authBaseUrl;

	@Override
	public ResponseModel<InviteResponse> sendHotelInvite(InviteRequest inviteRequest) {
		InvitationDetails invitationDetails = sendHotelEmail(inviteRequest);
		ResponseModel<InviteResponse> response = new ResponseModel<>();
		InviteResponse inviteResponse = new InviteResponse();
		inviteResponse.setInvitationId(invitationDetails.getId().toString());
		inviteResponse.setSendToEmail(invitationDetails.getSentToEmail());
		inviteResponse.setName(invitationDetails.getSendToName());
		response.setStatus(HttpStatus.CREATED);
		response.setMessage("Invitation details added successfully.");
		response.setData(inviteResponse);
		return response;
	}

	@Override
	public ResponseModel<InvitationDetailResponse> getInvitationById(ObjectId invitationId) {
		ResponseModel<InvitationDetailResponse> response = new ResponseModel<>();
		InvitationDetails details = invitationDetailsRepo.findById(invitationId).orElseThrow(
				() -> new RecordNotFoundException("Invitation details not found for id: "+invitationId)
		);
		InvitationDetailResponse invitationDetailResponse = new InvitationDetailResponse();
		invitationDetailResponse.setInvitationId(details.getId().toString());
		invitationDetailResponse.setSentToEmail(details.getSentToEmail());
		invitationDetailResponse.setCategory(details.getCategory());
		invitationDetailResponse.setStatus(details.getStatus());
		invitationDetailResponse.setSendToName(details.getSendToName());
		invitationDetailResponse.setHotelRequest(details.getHotelRequest());
		response.setStatus(HttpStatus.OK);
		response.setData(invitationDetailResponse);
		response.setMessage("Invitation details fetched successfully.");
		return response;
	}

	@Override
	public ResponseModel<Page<InviteResponse>> getAllPendingInvitations(String category, String status, int pageNo, int pageSize,
			String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy)); // Adjust for 0-based indexing
		Page<InvitationDetails> detailsPage = invitationDetailsRepo.findAllByCategoryAndStatus(pageable, category, Status.fromValue(status));
		List<InviteResponse> invitations = detailsPage.getContent().stream().map(this::mapToInviteResponse).toList();
		Page<InviteResponse> details = new PageImpl<>(invitations, pageable, detailsPage.getTotalElements());
		ResponseModel<Page<InviteResponse>> response = new ResponseModel<>();
		response.setMessage("Invitation details retrieved successfully");
		response.setStatus(HttpStatus.OK);
		response.setData(details);
		return response;
	}

	@Override
	public ResponseModel<InvitationDetails> getInvitationByEmail(String sentToEmail) {
		InvitationDetails invitations =  invitationDetailsRepo.findBySentToEmail(sentToEmail)
				.orElseThrow(() -> new RecordNotFoundException("Email not sent to " + sentToEmail));
		ResponseModel<InvitationDetails> response = new ResponseModel<>();
		response.setData(invitations);
		response.setMessage("Invitation details retrieved successfully");
		response.setStatus(HttpStatus.OK);
		return response;
	}

	@Override
	public ResponseModel<InvitationStatusResponse> updateInviteeStatus(ObjectId invitationId, String status) {
		ResponseModel<InvitationStatusResponse> response = new ResponseModel<>();
		InvitationDetails details = invitationDetailsRepo.findById(invitationId).orElseThrow(
				() -> new RecordNotFoundException("Invitation details not found for id: "+invitationId)
		);
		InvitationStatusResponse invitationStatusResponse = new InvitationStatusResponse();
		if(details.getStatus() == Status.fromValue(status))
			throw new BadRequestException("Status is already set to: " + details.getStatus());

		details.setStatus(Status.fromValue(status));
		InvitationDetails updatedInvitation;
		try {
			updatedInvitation = invitationDetailsRepo.save(details);
		} catch (Exception e) {
			throw new BadRequestException("Exception occurred while updating the status");
		}
		invitationStatusResponse.setUpdatedStatus(updatedInvitation.getStatus());
		invitationStatusResponse.setMessage("Status is successfully updated to: "+updatedInvitation.getStatus().getValue());
		response.setData(invitationStatusResponse);
		response.setStatus(HttpStatus.OK);
		response.setMessage("Status updated successfully");
		return response;
	}

	@Override
	public void sendRegistrationMail(InviteRequest inviteRequest) {

		try {
			String confirmationLink = String.format(CONFIRMATION_LINK, authBaseUrl, inviteRequest.getSentToEmail());
			Context thymeleafContext = new Context();
			thymeleafContext.setVariable("confirmationLink", confirmationLink);
			thymeleafContext.setVariable("name", inviteRequest.getSendToName());
			String emailContent = templateEngine.process("RegistrationEmailTemplate", thymeleafContext);
			emailSenderHelper.sendEmail(inviteRequest.getSentToEmail(), CONFIRMATION_SUBJECT, emailContent);
		} catch (Exception e) {
			throw new BadRequestException("Exception occurred while sending email.");
		}
	}

	public InviteResponse mapToInviteResponse(InvitationDetails invitationDetails) {
		InviteResponse inviteResponse = new InviteResponse();
		inviteResponse.setSendToEmail(invitationDetails.getSentToEmail());
		inviteResponse.setInvitationId(invitationDetails.getId().toString());
		inviteResponse.setName(invitationDetails.getHotelRequest().getName());
		inviteResponse.setAdminFullName(invitationDetails.getSendToName());
		inviteResponse
				.setPhoneNumber(String.join(invitationDetails.getHotelRequest().getManagerDetails().getCountryCode(),
						invitationDetails.getHotelRequest().getManagerDetails().getPhoneNumber()));
		return inviteResponse;
	}

	public InvitationDetails sendHotelEmail(InviteRequest inviteRequest) {
		boolean emailExists = invitationDetailsRepo.existsBySentToEmail(inviteRequest.getSentToEmail());
		if (emailExists) {
			throw new BadRequestException("This email " + inviteRequest.getSentToEmail() + " already exists in our system.");
		}
		InvitationDetails invitationDetails = new InvitationDetails();
		invitationDetails.setCategory(inviteRequest.getCategory().name());
		invitationDetails.setSendToName(inviteRequest.getSendToName());
		invitationDetails.setSentToEmail(inviteRequest.getSentToEmail());
		invitationDetails.setHotelRequest(inviteRequest.getHotelRequestBean());
		invitationDetails.setCreatedOn(LocalDateTime.now());
		invitationDetails.setCreatedBy("Super Admin");
		invitationDetails.setStatus(Status.PENDING);

		InvitationDetails savedInvitation;
		try {
			savedInvitation = invitationDetailsRepo.save(invitationDetails);

			String invitationLink = String.format(INVITATION_LINK, domainUrl, savedInvitation.getId(), savedInvitation.getSentToEmail());

			Context thymeleafContext = new Context();
			thymeleafContext.setVariable("invitationLink", invitationLink);
			String emailContent = templateEngine.process("HotelEmailTemplate", thymeleafContext);

			savedInvitation.setMessage(emailContent);
			savedInvitation.setInvitationUrl(invitationLink);
			savedInvitation.setTitle(INVITATION_SUBJECT);
			savedInvitation = invitationDetailsRepo.save(savedInvitation);

			String hotelEmail = inviteRequest.getSentToEmail();
			emailSenderHelper.sendEmail(hotelEmail, INVITATION_SUBJECT, emailContent);
		} catch (Exception e) {
			throw new BadRequestException("Exception occurred while sending email.");
		}

		return savedInvitation;
	}
}
