package com.notification.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomInvitationResponse {
	private int totalItems;
	private int pageNo;
	private int pageSize;
	private List<InviteResponse> invitations;

}
