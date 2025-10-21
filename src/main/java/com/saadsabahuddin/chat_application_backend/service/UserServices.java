package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.dto.UserDto;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.exceptions.UnauthenticatedException;
import com.saadsabahuddin.chat_application_backend.exceptions.UserExistsException;
import com.saadsabahuddin.chat_application_backend.repository.UserRepository;
import com.saadsabahuddin.chat_application_backend.utils.JwtUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public ResponseEntity<SuccessResponseDto<User>> register(User user) {
    User existing = userRepo.findByUserName(user.getUsername()).orElse(null);
    if (existing != null) {
      throw new UserExistsException("Username already taken!");
    }
    user.setUserPassword(bCrypt.encode(user.getPassword()));
    User createdUser = userRepo.save(user);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        new SuccessResponseDto<User>(createdUser, "User Created Successfully")
      );
  }

  public ResponseEntity<SuccessResponseDto<String>> verify(User user) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        user.getUsername(),
        user.getPassword()
      )
    );
    if (authentication.isAuthenticated()) {
      String token = jwtService.generateToken(user);
      return ResponseEntity
        .ok()
        .body(
          new SuccessResponseDto<String>(token, "User Logged In Successfully")
        );
    }
    throw new UnauthenticatedException("Invalid Credentials");
  }

  public ResponseEntity<SuccessResponseDto<List<String>>> getCreatedRooms(
    String userName
  ) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<String>>(
          user.getCreatedRooms(),
          "Created Rooms"
        )
      );
  }

  public ResponseEntity<SuccessResponseDto<List<String>>> getJoinedRooms(
    String userName
  ) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<String>>(
          user.getJoinedRooms(),
          "Created Rooms"
        )
      );
  }

  public ResponseEntity<SuccessResponseDto<UserDto>> getUser(String userName) {
    User user = userRepo
      .findByUserName(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<UserDto>(
          UserDto
            .builder()
            .userId(user.getUserId())
            .userName(user.getUsername())
            .aboutUser(user.getAboutUser())
            .build(),
          "Found User Successfully"
        )
      );
  }
}
