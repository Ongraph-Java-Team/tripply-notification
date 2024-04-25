package com.notification.controller;

import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.InviteResponse;
import com.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/send-hotel-invite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel<InviteResponse> sendHotelInvite(@RequestBody InviteRequest inviteRequest){
        log.info("Endpoint: /send-hotel-invite triggered with user: {}", inviteRequest.getSentToEmail());
        ResponseModel<InviteResponse> response = notificationService.sendHotelInvite(inviteRequest);
        log.info("Endpoint: /send-hotel-invite ends with user: {}", inviteRequest.getSentToEmail());
        return response;
    }
}
