package com.notification.controller;

import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.CustomInvitationResponse;
import com.notification.model.response.InvitationDetailResponse;
import com.notification.model.response.InviteResponse;
import com.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/send-hotel-invite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel<InviteResponse> sendHotelInvite(@RequestBody InviteRequest inviteRequest) {
        log.info("Endpoint: /send-hotel-invite triggered with user: {}", inviteRequest.getSentToEmail());
        ResponseModel<InviteResponse> response = notificationService.sendHotelInvite(inviteRequest);
        log.info("Endpoint: /send-hotel-invite ends with user: {}", inviteRequest.getSentToEmail());
        return response;
    }

    @GetMapping("/invite/{invitationId}")
    public ResponseModel<InvitationDetailResponse> getInviteById(@PathVariable("invitationId") ObjectId invitationId) {
        log.info("Endpoint: /get invite detail triggered: {}", invitationId);
        ResponseModel<InvitationDetailResponse> response = notificationService.getInvitationById(invitationId);
        log.info("Endpoint: /get invite detail ends: {}", invitationId);
        return response;
    }

    @GetMapping("/invitee/{category}")
    public CustomInvitationResponse getAllPendingInvitations(
            @PathVariable("category") String category,
            @RequestParam(name = "status", required = false, defaultValue = "PENDING") String status,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdOn") String sortBy) {
        log.info("Fetching pending invitations for category: {}, status: {}, page: {}, size: {}, sort by: {}",
                category, status, pageNo, pageSize, sortBy);
       CustomInvitationResponse response = notificationService.getAllPendingInvitations(category, status, pageNo, pageSize, sortBy);
        log.info("Fetched {} pending invitations.", response.getTotalItems());
        return response;
    }
}
