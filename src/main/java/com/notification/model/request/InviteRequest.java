package com.notification.model.request;

import com.notification.constant.InvitationCategory;
import com.notification.model.HotelRequestBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteRequest {

    private String sentToEmail;
    private InvitationCategory category;
    private String sendToName;
    private HotelRequestBean hotelRequestBean;

}
