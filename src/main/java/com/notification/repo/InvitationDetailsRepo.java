package com.notification.repo;

import com.notification.document.InvitationDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationDetailsRepo extends MongoRepository<InvitationDetails, ObjectId> {

}
