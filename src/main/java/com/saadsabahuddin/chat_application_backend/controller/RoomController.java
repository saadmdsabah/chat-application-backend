package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.CreateRoomDto;
import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.entities.Message;
import com.saadsabahuddin.chat_application_backend.entities.Room;
import com.saadsabahuddin.chat_application_backend.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    Room room = roomService.createRoom(
      roomId,
      createRoomDto.getUserName(),
      createRoomDto.isPrivateRoom()
    );
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(new SuccessResponseDto<Room>(room, "Room Created Successfully"));
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<SuccessResponseDto<Room>> joinRoom(
    @PathVariable String roomId,
    @RequestParam String userName
  ) {
    Room room = roomService.joinRoom(roomId, userName);
    return ResponseEntity
      .ok()
      .body(new SuccessResponseDto<Room>(room, "Found Room Successfully"));
  }

  @GetMapping("/{roomId}/messages")
  public ResponseEntity<SuccessResponseDto<List<Message>>> getMessages(
    @PathVariable String roomId,
    @RequestParam(defaultValue = "0", required = false) int page,
    @RequestParam(defaultValue = "10", required = false) int size,
    @RequestParam String userName
  ) {
    List<Message> messages = roomService.getMessages(
      roomId,
      page,
      size,
      userName
    );
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<Message>>(
          messages,
          "Found Messages Successfully"
        )
      );
  }

  @GetMapping("/entryPossible/{roomId}")
  public ResponseEntity<SuccessResponseDto<Boolean>> checkIfEntryPossibleForUser(
    @PathVariable String roomId,
    @RequestParam String userName
  ) {
    Boolean checkIfEntryPossibleForUser = roomService.checkIfEntryPossibleForUser(
      roomId,
      userName
    );
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<Boolean>(
          checkIfEntryPossibleForUser,
          "Entry Possible"
        )
      );
  }

  @GetMapping("/{roomId}/creater")
  public ResponseEntity<SuccessResponseDto<String>> getRoomCreater(
    @PathVariable String roomId
  ) {
    String roomCreater = roomService.getRoomCreater(roomId);
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<String>(roomCreater, "Fetched Successfully")
      );
  }
}
