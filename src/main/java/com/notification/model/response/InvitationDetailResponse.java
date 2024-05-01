package com.notification.model.response;

import com.notification.model.HotelRequestBean;
import com.notification.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDetailResponse  {
    private String invitationId;
    private String sentToEmail;
    private String category;
    private Status status;
    private String sendToName;
    private HotelRequestBean hotelRequest;
}
