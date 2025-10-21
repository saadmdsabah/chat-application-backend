package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.CreateRoomDto;
import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.entities.Message;
import com.saadsabahuddin.chat_application_backend.entities.Room;
import com.saadsabahuddin.chat_application_backend.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping("/{roomId}")
  public ResponseEntity<SuccessResponseDto<Room>> createRoom(
    @PathVariable String roomId,
    @RequestBody CreateRoomDto createRoomDto
  ) {
    return roomService.createRoom(
      roomId,
      createRoomDto.getUserName(),
      createRoomDto.isPrivateRoom()
    );
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<SuccessResponseDto<Room>> joinRoom(
    @PathVariable String roomId,
    @RequestParam String userName
  ) {
    return roomService.joinRoom(roomId, userName);
  }

  @GetMapping("/{roomId}/messages")
  public ResponseEntity<SuccessResponseDto<List<Message>>> getMessages(
    @PathVariable String roomId,
    @RequestParam(defaultValue = "0", required = false) int page,
    @RequestParam(defaultValue = "10", required = false) int size,
    @RequestParam String userName
  ) {
    return roomService.getMessages(roomId, page, size, userName);
  }

  @GetMapping("/entryPossible/{roomId}")
  public ResponseEntity<SuccessResponseDto<Boolean>> checkIfEntryPossibleForUser(
    @PathVariable String roomId,
    @RequestParam String userName
  ) {
    return roomService.checkIfEntryPossibleForUser(roomId, userName);
  }

  @GetMapping("/{roomId}/creater")
  public ResponseEntity<SuccessResponseDto<String>> getRoomCreater(
    @PathVariable String roomId
  ) {
    return roomService.getRoomCreater(roomId);
  }
}
