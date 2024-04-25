package com.notification.document;

import com.notification.model.HotelRequestBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "invitation_details")
public class InvitationDetails extends BaseDocument {

    @Field("sent_to_email")
    private String sentToEmail;
    @Field("category")
    private String category;
    @Field("sent_to_name")
    private String sendToName;
    @Field("title")
    private String title;
    @Field("message")
    private String message;
    @Field("invitation_url")
    private String invitationUrl;
    @Field("hotel_request")
    private HotelRequestBean hotelRequest;

}
