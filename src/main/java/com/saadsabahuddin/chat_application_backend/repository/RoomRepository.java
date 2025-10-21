package com.saadsabahuddin.chat_application_backend.repository;

import com.saadsabahuddin.chat_application_backend.entities.Room;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
  Optional<Room> findByRoomId(String roomId);
}
