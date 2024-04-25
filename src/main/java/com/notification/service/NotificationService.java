package com.notification.service;

import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.InviteResponse;

public interface NotificationService {

    ResponseModel<InviteResponse> sendHotelInvite(InviteRequest inviteRequest);

}
