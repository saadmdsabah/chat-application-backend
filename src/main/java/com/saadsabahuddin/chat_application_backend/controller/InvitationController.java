package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.CreateInvitationDto;
import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.dto.UpdateInvitationStatusDto;
import com.saadsabahuddin.chat_application_backend.entities.Invitation;
import com.saadsabahuddin.chat_application_backend.service.InvitationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationController {

  private final InvitationService invitationService;

  @PostMapping("/create")
  public ResponseEntity<SuccessResponseDto<Invitation>> createInvitation(
    @RequestBody CreateInvitationDto createInvitationDto
  ) {
    Invitation invitation = invitationService.createInvitation(
      createInvitationDto
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

  @PostMapping("/updateStatus")
  public ResponseEntity<SuccessResponseDto<Boolean>> updateStatusOfInvitation(
    @RequestBody UpdateInvitationStatusDto updateInvitationStatusDto
  ) {
    Boolean updateStatusOfInvitation = invitationService.updateStatusOfInvitation(
      updateInvitationStatusDto
    );
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<>(
          updateStatusOfInvitation,
          "Invitation accepted successfully"
        )
      );
  }

  @GetMapping("/sender/{userName}")
  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForASender(
    @PathVariable String userName
  ) {
    List<Invitation> invitations = invitationService.getAllInvitationsForASender(
      userName
    );
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<Invitation>>(
          invitations,
          "Found Invitations Successfully"
        )
      );
  }

  @GetMapping("/receiver/{userName}")
  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForAReciever(
    @PathVariable String userName
  ) {
    List<Invitation> invitations = invitationService.getAllInvitationsForAReciever(userName);
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
