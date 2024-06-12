package com.notification.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenRequest {
    String token;
    String sendToName;
    String sendToEmail;
}
