package com.notification.service;

import com.notification.document.InvitationDetails;
import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.response.InvitationDetailResponse;
import com.notification.model.response.InviteResponse;
import org.bson.types.ObjectId;

public interface NotificationService {

    ResponseModel<InviteResponse> sendHotelInvite(InviteRequest inviteRequest);

    ResponseModel<InvitationDetailResponse> getInvitationById(ObjectId invitationId);

}
