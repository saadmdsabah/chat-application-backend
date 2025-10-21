package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.dto.UserDto;
import com.saadsabahuddin.chat_application_backend.service.UserServices;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserServices userServices;

  @GetMapping("/{userName}/createdRooms")
  public ResponseEntity<SuccessResponseDto<List<String>>> getCreatedRooms(
    @PathVariable String userName
  ) {
    List<String> createdRooms = userServices.getCreatedRooms(userName);
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<List<String>>(createdRooms, "Created Rooms")
      );
  }

  @GetMapping("/{userName}/joinedRooms")
  public ResponseEntity<SuccessResponseDto<List<String>>> getJoinedRooms(
    @PathVariable String userName
  ) {
    List<String> joinedRooms = userServices.getJoinedRooms(userName);
    return ResponseEntity
      .ok()
      .body(new SuccessResponseDto<List<String>>(joinedRooms, "Created Rooms"));
  }

  @GetMapping("/user/{userName}")
  public ResponseEntity<SuccessResponseDto<UserDto>> getMethodName(
    @PathVariable String userName
  ) {
    UserDto user = userServices.getUser(userName);
    return ResponseEntity
      .ok()
      .body(new SuccessResponseDto<UserDto>(user, "Found User Successfully"));
  }
}
