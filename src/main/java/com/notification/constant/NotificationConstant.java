package com.notification.constant;

import org.springframework.beans.factory.annotation.Value;

public class NotificationConstant {


    public static final String INVITATION_LINK = "%s/set-password?inviteId=%s&inviteeEmail=%s";
    public static final String INVITATION_SUBJECT = "Tripply:- Hotel Onboard Invite";
    public static final String PASSWORD_RESET_LINK = "/auth/reset-password?token=";
    public static final String PASSWORD_RESET_SUBJECT = "Tripply:- Reset Your Password";

}
