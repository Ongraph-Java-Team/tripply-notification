package com.notification.service.impl;

import com.notification.document.InvitationDetails;
import com.notification.exception.BadRequestException;
import com.notification.exception.RecordNotFoundException;
import com.notification.helper.EmailSenderHelper;
import com.notification.model.ResponseModel;
import com.notification.model.Status;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.InvitationDetailResponse;
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

import static com.notification.constant.NotificationConstant.INVITATION_LINK;
import static com.notification.constant.NotificationConstant.INVITATION_SUBJECT;

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
		Page<InvitationDetails> detailsPage = invitationDetailsRepo.findAllByCategory(pageable, category);
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
				.orElseThrow(() -> new RecordNotFoundException("Email not sent " + sentToEmail));
		ResponseModel<InvitationDetails> response = new ResponseModel<>();
		response.setData(invitations);
		response.setMessage("Invitation details retrieved successfully");
		response.setStatus(HttpStatus.OK);
		return response;
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

			Context thymeleafContext = new Context();
			thymeleafContext.setVariable("sentToName", inviteRequest.getSendToName());
			thymeleafContext.setVariable("invitationLink", INVITATION_LINK);
			String emailContent = templateEngine.process("HotelEmailTemplate", thymeleafContext);

			savedInvitation.setMessage(emailContent);
			savedInvitation.setInvitationUrl(INVITATION_LINK);
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
