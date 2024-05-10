package com.notification.repo;

import com.notification.document.InvitationDetails;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvitationDetailsRepo extends MongoRepository<InvitationDetails, ObjectId> {
    Page<InvitationDetails> findAllByCategory(Pageable pageable, String category);
    boolean existsBySentToEmail(String email);
    Optional<InvitationDetails> findBySentToEmail(String sentToEmail);
}
