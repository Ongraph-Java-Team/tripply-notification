package com.notification.repo;

import com.notification.document.PasswordResetTokenDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordResetTokenDetailsRepository extends MongoRepository<PasswordResetTokenDetails, ObjectId> {
}
