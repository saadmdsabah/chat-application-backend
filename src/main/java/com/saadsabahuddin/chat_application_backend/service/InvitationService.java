package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.dto.CreateInvitationDto;
import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.dto.UpdateInvitationStatusDto;
import com.saadsabahuddin.chat_application_backend.entities.Invitation;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.repository.InvitationRepository;
import com.saadsabahuddin.chat_application_backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

  private final InvitationRepository invitationRepository;
  private final UserRepository userRepository;

  public ResponseEntity<SuccessResponseDto<Invitation>> createInvitation(
    CreateInvitationDto createInvitationDto
  ) {
    if (
      createInvitationDto.getReceiver().equals(createInvitationDto.getSender())
    ) {
      throw new IllegalArgumentException(
        "Sender and receiver cannot be the same"
      );
    }

    userRepository
      .findByUserName(createInvitationDto.getReceiver())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    userRepository
      .findByUserName(createInvitationDto.getSender())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (
      invitationRepository
        .findByReceiverAndSender(
          createInvitationDto.getReceiver(),
          createInvitationDto.getSender()
        )
        .isPresent()
    ) {
      throw new IllegalArgumentException("Invitation already exists");
    }
    Invitation invitation = invitationRepository.save(
      Invitation
        .builder()
        .createdAt(LocalDateTime.now())
        .receiver(createInvitationDto.getReceiver())
        .sender(createInvitationDto.getSender())
        .roomId(createInvitationDto.getRoomId())
        .status("pending")
        .build()
    );

    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<Invitation>(
          invitation,
          "Invitation created successfully"
        )
      );
  }

  public ResponseEntity<SuccessResponseDto<Boolean>> updateStatusOfInvitation(
    UpdateInvitationStatusDto updateInvitationStatusDto
  ) {
    if (updateInvitationStatusDto.getStatus().equals("accepted")) {
      // add room to user joined rooms
      invitationRepository
        .findByReceiverAndSender(
          updateInvitationStatusDto.getReceiver(),
          updateInvitationStatusDto.getSender()
        )
        .orElseThrow(() ->
          new IllegalArgumentException("Invitation does not exist")
        );
      User sender = userRepository
        .findByUserName(updateInvitationStatusDto.getSender())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      sender.getJoinedRooms().add(updateInvitationStatusDto.getRoomId());
      userRepository.save(sender);
      invitationRepository.deleteByReceiverAndSender(
        updateInvitationStatusDto.getReceiver(),
        updateInvitationStatusDto.getSender()
      );
      return ResponseEntity
        .ok()
        .body(
          new SuccessResponseDto<>(true, "Invitation accepted successfully")
        );
    }
    invitationRepository
      .findByReceiverAndSender(
        updateInvitationStatusDto.getReceiver(),
        updateInvitationStatusDto.getSender()
      )
      .orElseThrow(() ->
        new IllegalArgumentException("Invitation does not exist")
      );
    invitationRepository.deleteByReceiverAndSender(
      updateInvitationStatusDto.getReceiver(),
      updateInvitationStatusDto.getSender()
    );
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<>(false, "Invitation rejected successfully")
      );
  }

  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForASender(
    String userName
  ) {
    List<Invitation> invitations = invitationRepository
      .findBySender(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<Invitation>>(
          invitations,
          "Found Invitations Successfully"
        )
      );
  }

  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForAReciever(
    String userName
  ) {
    List<Invitation> invitations = invitationRepository
      .findByReceiver(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<Invitation>>(
          invitations,
          "Found Invitations Successfully"
        )
      );
  }
}
