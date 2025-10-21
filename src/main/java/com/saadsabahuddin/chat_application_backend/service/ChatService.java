package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.dto.MessageDto;
import com.saadsabahuddin.chat_application_backend.entities.Message;
import com.saadsabahuddin.chat_application_backend.entities.Room;
import com.saadsabahuddin.chat_application_backend.exceptions.RoomNotFoundException;
import com.saadsabahuddin.chat_application_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final RoomRepository roomRepository;

  public Message sendMessage(String roomId, MessageDto messageRequest) {
    Room room = roomRepository
      .findByRoomId(messageRequest.getRoomId())
      .orElseThrow(() -> new RoomNotFoundException("Room not found"));
    Message message = new Message(
      messageRequest.getSender(),
      messageRequest.getContent()
    );
    room.getMessages().add(message);
    roomRepository.save(room);
    return message;
  }
}
