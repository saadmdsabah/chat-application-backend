package com.saadsabahuddin.chat_application_backend.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

  @Id
  private String userId;

  private String userName;
  private String userPassword;
  private String aboutUser;
  private List<String> createdRooms = new ArrayList<>();
  private List<String> joinedRooms = new ArrayList<>();

  public User(String userName, String userPassword) {
    this.userName = userName;
    this.userPassword = userPassword;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return this.userPassword;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }
}
