package com.notification.model.response;

import com.notification.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationStatusResponse {
    private String message;
    private Status updatedStatus;
}
