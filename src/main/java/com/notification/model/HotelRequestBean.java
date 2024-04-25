package com.notification.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class HotelRequestBean {

    @Field("name")
    private String name;
    @Field("address")
    private String address;
    @Field("city")
    private String city;
    @Field("state_id")
    private String stateId;
    @Field("country_id")
    private String countryId;
    @Field("description")
    private String description;
    @Field("website")
    private String website;
    @Field("amenities")
    private List<Amenity> amenities;
    @Field("managerDetails")
    private ManagerDetails managerDetails;

}