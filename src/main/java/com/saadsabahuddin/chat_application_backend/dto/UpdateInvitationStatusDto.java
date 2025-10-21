package com.saadsabahuddin.chat_application_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvitationStatusDto {

  private String status;
  private String roomId;
  private String sender;
  private String receiver;
}
