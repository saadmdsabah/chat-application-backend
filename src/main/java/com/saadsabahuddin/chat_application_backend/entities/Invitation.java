package com.saadsabahuddin.chat_application_backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invitations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitation implements Serializable {

  @Id
  private String id;

  private String roomId;
  private String sender; // username or userId
  private String receiver; // username or userId
  private String status; // PENDING, ACCEPTED, REJECTED
  private LocalDateTime createdAt;
}
