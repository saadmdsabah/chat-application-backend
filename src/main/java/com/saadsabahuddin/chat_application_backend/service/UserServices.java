package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.dto.UserDto;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.exceptions.UnauthenticatedException;
import com.saadsabahuddin.chat_application_backend.exceptions.UserExistsException;
import com.saadsabahuddin.chat_application_backend.repository.UserRepository;
import com.saadsabahuddin.chat_application_backend.utils.JwtUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServices {

  private final UserRepository userRepo;
  private final BCryptPasswordEncoder bCrypt;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtService;

  public User register(User user) {
    User existing = userRepo.findByUserName(user.getUsername()).orElse(null);
    if (existing != null) {
      throw new UserExistsException("Username already taken!");
    }
    user.setUserPassword(bCrypt.encode(user.getPassword()));
    User createdUser = userRepo.save(user);
    return createdUser;
  }

  public String verify(User user) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        user.getUsername(),
        user.getPassword()
      )
    );
    if (authentication.isAuthenticated()) {
      String token = jwtService.generateToken(user);
      return token;
    }
    throw new UnauthenticatedException("Invalid Credentials");
  }

  public List<String> getCreatedRooms(String userName) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return user.getCreatedRooms();
  }

  public List<String> getJoinedRooms(String userName) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return user.getJoinedRooms();
  }

  public UserDto getUser(String userName) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return UserDto
      .builder()
      .userId(user.getUserId())
      .userName(user.getUsername())
      .aboutUser(user.getAboutUser())
      .build();
  }
}
