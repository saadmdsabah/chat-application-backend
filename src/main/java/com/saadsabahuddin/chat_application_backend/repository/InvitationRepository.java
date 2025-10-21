package com.saadsabahuddin.chat_application_backend.repository;

import com.saadsabahuddin.chat_application_backend.entities.Invitation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository
  extends MongoRepository<Invitation, String> {
  Optional<List<Invitation>> findBySender(String sender);

  Optional<List<Invitation>> findByReceiver(String receiver);

  Optional<Invitation> findByReceiverAndSender(String receiver, String sender);

  void deleteByReceiverAndSender(String receiver, String sender);
}
