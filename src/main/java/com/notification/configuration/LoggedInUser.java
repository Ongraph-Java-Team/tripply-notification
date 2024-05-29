package com.notification.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class LoggedInUser {
    private String userId;
    private String email;
    private List<String> roles;
}
