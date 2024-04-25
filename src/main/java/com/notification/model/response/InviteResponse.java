package com.notification.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteResponse {

    private String invitationId;
    private String name;
    private String email;

}
