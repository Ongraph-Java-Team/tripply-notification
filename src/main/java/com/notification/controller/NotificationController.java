package com.notification.controller;

import com.notification.document.InvitationDetails;
import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.CustomInvitationResponse;
import com.notification.model.response.InvitationDetailResponse;
import com.notification.model.response.InviteResponse;
import com.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Send Invitation to hotel.",
            description = "This API will send invitation to hotel and will throw exception if email invitation already exists in the system.")
    @PostMapping(value = "/send-hotel-invite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<InviteResponse>> sendHotelInvite(@RequestBody InviteRequest inviteRequest) {
        log.info("Endpoint: /send-hotel-invite triggered with user: {}", inviteRequest.getSentToEmail());
        ResponseModel<InviteResponse> response = notificationService.sendHotelInvite(inviteRequest);
        log.info("Endpoint: /send-hotel-invite ends with user: {}", inviteRequest.getSentToEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invite/{invitationId}")
    public ResponseEntity<ResponseModel<InvitationDetailResponse>> getInviteById(@PathVariable("invitationId") ObjectId invitationId) {
        log.info("Endpoint: /get invite detail triggered: {}", invitationId);
        ResponseModel<InvitationDetailResponse> response = notificationService.getInvitationById(invitationId);
        log.info("Endpoint: /get invite detail ends: {}", invitationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-invitation/{sentToEmail}")
    public ResponseEntity<ResponseModel<InvitationDetails>> getInviteById(@PathVariable("sentToEmail") String sentToEmail) {
        log.info("Endpoint: /get invite detail triggered: {}", sentToEmail);
        ResponseModel<InvitationDetails> response = notificationService.getInvitationByEmail(sentToEmail);
        log.info("Endpoint: /get invite detail ends: {}", sentToEmail);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all pending invitations",
            description = "This API will give all the pending invitations.")
    @GetMapping("/invitee/{category}")
    public ResponseEntity<ResponseModel<Page<InviteResponse>>> getAllPendingInvitations(
            @PathVariable("category") String category,
            @RequestParam(name = "status", required = false, defaultValue = "PENDING") String status,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdOn") String sortBy) {
        log.info("Fetching pending invitations for category: {}, status: {}, page: {}, size: {}, sort by: {}",
                category, status, pageNo, pageSize, sortBy);
        ResponseModel<Page<InviteResponse>> response = notificationService.getAllPendingInvitations(category, status, pageNo, pageSize, sortBy);
        log.info("Fetched pending invitations.");
        return ResponseEntity.ok(response);
    }
}
