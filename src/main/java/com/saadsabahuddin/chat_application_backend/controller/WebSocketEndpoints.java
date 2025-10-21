package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.CreateInvitationDto;
import com.saadsabahuddin.chat_application_backend.dto.CreateRoomDto;
import com.saadsabahuddin.chat_application_backend.dto.MessageDto;
import com.saadsabahuddin.chat_application_backend.dto.UpdateInvitationStatusDto;
import com.saadsabahuddin.chat_application_backend.entities.Invitation;
import com.saadsabahuddin.chat_application_backend.entities.Message;
import com.saadsabahuddin.chat_application_backend.entities.Room;
import com.saadsabahuddin.chat_application_backend.service.ChatService;
import com.saadsabahuddin.chat_application_backend.service.InvitationService;
import com.saadsabahuddin.chat_application_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class WebSocketEndpoints {

  private final ChatService chatService;
  private final RoomService roomService;
  private final InvitationService invitationService;

  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/sendMessage/{roomId}")
  @SendTo("/topic/rooms/{roomId}")
  public Message sendMessage(
    @DestinationVariable String roomId,
    @RequestBody MessageDto messageRequest
  ) {
    return chatService.sendMessage(roomId, messageRequest);
  }

  @MessageMapping("/createRoom/{roomId}")
  public void createRoom(
    @DestinationVariable String roomId,
    @RequestBody CreateRoomDto createRoomDto
  ) {
    Room room = roomService.createRoom(
      roomId,
      createRoomDto.getUserName(),
      createRoomDto.isPrivateRoom()
    );
    if (room != null) {
      messagingTemplate.convertAndSend(
        "/topic/rooms/" + createRoomDto.getUserName(),
        room.getRoomId()
      );
    }
  }

  @MessageMapping("/addRoom/{userName}/{roomId}")
  public void addRoom(
    @DestinationVariable String userName,
    @DestinationVariable String roomId
  ) {
    Room joinRoom = roomService.joinRoom(roomId, userName);
    if (joinRoom != null) {
      messagingTemplate.convertAndSend(
        "/topic/rooms/" + userName,
        joinRoom.getRoomId()
      );
    }
  }

  @MessageMapping("/createInvitation")
  public void createInvitation(
    @RequestBody CreateInvitationDto createInvitationDto
  ) {
    Invitation invitation = invitationService.createInvitation(
      createInvitationDto
    );
    if (invitation != null) {
      messagingTemplate.convertAndSend(
        "/topic/sentInvitations/" + createInvitationDto.getSender(),
        invitation
      );

      messagingTemplate.convertAndSend(
        "/topic/receivedInvitations/" + createInvitationDto.getReceiver(),
        invitation
      );
    }
  }

  @MessageMapping("/updateStatusOfInvitation")
  public void removeSentInvitation(
    @RequestBody UpdateInvitationStatusDto updateInvitationStatusDto
  ) {
    Boolean updateStatusOfInvitation = invitationService.updateStatusOfInvitation(
      updateInvitationStatusDto
    );
    if (updateStatusOfInvitation != null && updateStatusOfInvitation) {
      messagingTemplate.convertAndSend(
        "/topic/remove/sentInvitations/" +
        updateInvitationStatusDto.getSender(),
        updateInvitationStatusDto.getRoomId()
      );

      messagingTemplate.convertAndSend(
        "/topic/rooms/" + updateInvitationStatusDto.getSender(),
        updateInvitationStatusDto.getRoomId()
      );
    } else {
      messagingTemplate.convertAndSend(
        "/topic/remove/sentInvitations/" +
        updateInvitationStatusDto.getSender(),
        updateInvitationStatusDto.getRoomId()
      );
    }
  }
}
