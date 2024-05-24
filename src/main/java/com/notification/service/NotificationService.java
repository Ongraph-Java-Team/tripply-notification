package com.notification.service;

import com.notification.document.InvitationDetails;
import com.notification.model.ResponseModel;
import com.notification.model.request.InviteRequest;
import com.notification.model.request.StatusUpdateRequest;
import com.notification.model.response.InvitationDetailResponse;
import com.notification.model.response.InvitationStatusResponse;
import com.notification.model.response.InviteResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface NotificationService {

    ResponseModel<InviteResponse> sendHotelInvite(InviteRequest inviteRequest);

    ResponseModel<InvitationDetailResponse> getInvitationById(ObjectId invitationId);

    ResponseModel<Page<InviteResponse>> getAllPendingInvitations(String category, String status, int pageNo, int pageSize, String sortBy);

    ResponseModel<InvitationDetails> getInvitationByEmail(String sentToEmail);

    ResponseModel<InvitationStatusResponse> updateInviteeStatus(ObjectId invitationId, StatusUpdateRequest statusUpdateRequest);
}
