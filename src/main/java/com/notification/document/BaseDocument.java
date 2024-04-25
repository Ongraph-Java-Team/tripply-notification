package com.notification.document;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
public class BaseDocument {

    @Id
    private ObjectId id;

    @Field("created_by")
    private String createdBy;

    @Field("created_on")
    private LocalDateTime createdOn;

    @Field("modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Field("modified_on")
    @LastModifiedDate
    private LocalDateTime modifiedOn;
}
