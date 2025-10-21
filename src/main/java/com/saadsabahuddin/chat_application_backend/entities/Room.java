package com.saadsabahuddin.chat_application_backend.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  @Id
  private String id;

  private String createdBy;
  private boolean privateRoom;
  private String roomId;
  private List<Message> messages = new ArrayList<>();
}
