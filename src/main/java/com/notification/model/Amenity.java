package com.notification.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Amenity {

    @Field("name")
    private String name;
    @Field("description")
    private String description;

}
