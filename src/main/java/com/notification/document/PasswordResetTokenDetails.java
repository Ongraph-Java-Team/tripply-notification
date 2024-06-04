package com.notification.document;

import com.notification.dto.User;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "password_reset_details")
public class PasswordResetTokenDetails {

    @Field("password_reset_token")
    private String token;
    @Field("user_email")
    private String userEmail;
    @Field("password_reset_link")
    private String passwordResetLink;
}
