package com.saadsabahuddin.chat_application_backend.repository;

import com.saadsabahuddin.chat_application_backend.entities.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUserName(String username);
}
