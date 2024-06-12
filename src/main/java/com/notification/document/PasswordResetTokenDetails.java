package com.notification.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "password_reset_details")
public class PasswordResetTokenDetails {

    @Field("password_reset_token")
    private String token;
    @Field("user_email")
    private String sendToEmail;
    @Field("password_reset_link")
    private String passwordResetLink;
    @Field("created_on")
    private Date createdOn;
}
