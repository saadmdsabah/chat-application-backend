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
    return invitationService.createInvitation(createInvitationDto);
  }

  @PostMapping("/updateStatus")
  public ResponseEntity<SuccessResponseDto<Boolean>> updateStatusOfInvitation(
    @RequestBody UpdateInvitationStatusDto updateInvitationStatusDto
  ) {
    return invitationService.updateStatusOfInvitation(
      updateInvitationStatusDto
    );
  }

  @GetMapping("/sender/{userName}")
  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForASender(
    @PathVariable String userName
  ) {
    return invitationService.getAllInvitationsForASender(userName);
  }

  @GetMapping("/receiver/{userName}")
  public ResponseEntity<SuccessResponseDto<List<Invitation>>> getAllInvitationsForAReciever(
    @PathVariable String userName
  ) {
    return invitationService.getAllInvitationsForAReciever(userName);
  }
}
