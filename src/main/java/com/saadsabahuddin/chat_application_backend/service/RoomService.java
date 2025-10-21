package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.entities.Message;
import com.saadsabahuddin.chat_application_backend.entities.Room;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.exceptions.RoomExistsException;
import com.saadsabahuddin.chat_application_backend.exceptions.RoomNotFoundException;
import com.saadsabahuddin.chat_application_backend.repository.RoomRepository;
import com.saadsabahuddin.chat_application_backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  public Room createRoom(String roomId, String userName, boolean isPrivate) {
    Room room = roomRepository.findByRoomId(roomId).orElse(null);
    if (room != null) {
      throw new RoomExistsException("Room Already Exists");
    }
    room = new Room();
    room.setRoomId(roomId);
    room.setCreatedBy(userName);
    room.setPrivateRoom(isPrivate);
    Room savedRoom = roomRepository.save(room);
    User user = userRepository
      .findByUserName(userName)
      .orElseThrow(() ->
        new UsernameNotFoundException("User with Username: " + userName)
      );
    if (!user.getCreatedRooms().contains(roomId)) {
      user.getCreatedRooms().add(roomId);
    }
    if (!user.getJoinedRooms().contains(roomId)) {
      user.getJoinedRooms().add(roomId);
    }
    userRepository.save(user);
    return savedRoom;
  }

  public Room joinRoom(String roomId, String userName) {
    Room room = roomRepository
      .findByRoomId(roomId)
      .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    User user = userRepository
      .findByUserName(userName)
      .orElseThrow(() ->
        new UsernameNotFoundException("User with Username: " + userName)
      );
    if (room.isPrivateRoom() || user.getJoinedRooms().contains(roomId)) {
      throw new IllegalArgumentException(
        "Room is private or user already joined"
      );
    }
    user.getJoinedRooms().add(roomId);
    userRepository.save(user);
    return room;
  }

  public List<Message> getMessages(
    String roomId,
    int page,
    int size,
    String userName
  ) {
    Room room = roomRepository
      .findByRoomId(roomId)
      .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    User user = userRepository
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    if (!user.getJoinedRooms().contains(roomId)) {
      throw new IllegalArgumentException("User not joined room");
    }
    List<Message> messages = room.getMessages();
    int start = Math.max(0, messages.size() - (page + 1) * size);
    int end = Math.min(messages.size(), start + size);
    return messages.subList(start, end);
  }

  public Boolean checkIfEntryPossibleForUser(String roomId, String userName) {
    User user = userRepository
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Room room = roomRepository
      .findByRoomId(roomId)
      .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    if (user.getJoinedRooms().contains(roomId) || !room.isPrivateRoom()) {
      return true;
    }
    return false;
  }

  public String getRoomCreater(String roomId) {
    Room room = roomRepository
      .findByRoomId(roomId)
      .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    return room.getCreatedBy();
  }
}
