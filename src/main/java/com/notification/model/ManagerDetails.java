package com.notification.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ManagerDetails {

    @Field("email")
    private String email;
    @Field("phone_number")
    private String phoneNumber;
    @Field("country_code")
    private String countryCode;
    @Field("first_name")
    private String firstName;
    @Field("last_name")
    private String lastName;

}
