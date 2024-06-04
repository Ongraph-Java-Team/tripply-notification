package com.notification.constant;

import org.springframework.beans.factory.annotation.Value;

public class NotificationConstant {
    @Value("${application.auth.base-url}")
    public static String BASE_AUTH_URL;

    public static final String INVITATION_LINK = "%s/set-password?inviteId=%s&inviteeEmail=%s";
    public static final String INVITATION_SUBJECT = "Tripply:- Hotel Onboard Invite";
    public static final String PASSWORD_RESET_LINK = BASE_AUTH_URL + "/auth/reset-password?token=";
    public static final String PASSWORD_RESET_SUBJECT = "Tripply:- Reset Your Password";

}
