package com.notification.service.impl;

import com.notification.document.InvitationDetails;
import com.notification.helper.EmailSenderHelper;
import com.notification.model.HotelRequestBean;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public ResponseModel<InviteResponse> sendHotelInvite(InviteRequest inviteRequest){
        InvitationDetails invitationDetails = sendHotelEmail(inviteRequest);
        ResponseModel<InviteResponse> response = new ResponseModel<>();
        InviteResponse inviteResponse = new InviteResponse();
        inviteResponse.setInvitationId(invitationDetails.getId().toString());
        inviteResponse.setEmail(invitationDetails.getSentToEmail());
        inviteResponse.setName(invitationDetails.getSendToName());
        response.setStatus(HttpStatus.CREATED);
        response.setMessage("Invitation details added successfully.");
        response.setData(inviteResponse);
        return response;
    }

    @Override
    public ResponseModel<InvitationDetailResponse> getInvitationById(ObjectId invitationId) {
        ResponseModel<InvitationDetailResponse> response = new ResponseModel<>();
        Optional<InvitationDetails> invitationDetails = invitationDetailsRepo.findById(invitationId);
        if (invitationDetails.isPresent()) {
            InvitationDetails details = invitationDetails.get();

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
        } else {
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setMessage("Invitation not found");
        }
        return response;
    }

    public InvitationDetails sendHotelEmail(InviteRequest inviteRequest) {
        InvitationDetails invitationDetails = new InvitationDetails();
        invitationDetails.setCategory(inviteRequest.getCategory().name());
        invitationDetails.setSendToName(inviteRequest.getSendToName());
        invitationDetails.setSentToEmail(inviteRequest.getSentToEmail());
        invitationDetails.setHotelRequest(inviteRequest.getHotelRequestBean());
        invitationDetails.setCreatedOn(LocalDateTime.now());
        invitationDetails.setCreatedBy("Super Admin");
        invitationDetails.setStatus(Status.PENDING);
        InvitationDetails savedInvitation = invitationDetailsRepo.save(invitationDetails);
        try {

            Context thymeleafContext = new Context();
            thymeleafContext.setVariable("sentToName", inviteRequest.getSendToName());
            thymeleafContext.setVariable("invitationLink", INVITATION_LINK);

            String emailContent = templateEngine.process("HotelEmailTemplate", thymeleafContext);

            TemplateEngine templateEngine1 = new SpringTemplateEngine();
            StringTemplateResolver resolver = new StringTemplateResolver();
            resolver.setTemplateMode(emailContent);
            templateEngine1.setTemplateResolver(resolver);

            String content = templateEngine1.process(emailContent, thymeleafContext);

            String therapistEmail = inviteRequest.getSentToEmail();
            emailSenderHelper.sendEmail(therapistEmail, INVITATION_SUBJECT, content);
            savedInvitation.setMessage(emailContent);
            savedInvitation.setInvitationUrl(INVITATION_LINK);
            savedInvitation.setTitle(INVITATION_SUBJECT);
            savedInvitation = invitationDetailsRepo.save(savedInvitation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedInvitation;
    }
}
