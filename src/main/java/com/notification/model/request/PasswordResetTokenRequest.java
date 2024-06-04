package com.notification.model.request;

import com.notification.dto.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenRequest {
    String token;
    User user;
}
