package com.saadsabahuddin.chat_application_backend.controller;

import com.saadsabahuddin.chat_application_backend.dto.SuccessResponseDto;
import com.saadsabahuddin.chat_application_backend.service.MongoUserDetailsService;
import com.saadsabahuddin.chat_application_backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class VerifyToken {

  private final JwtUtils jwtService;
  private final ApplicationContext context;

  @PostMapping("/verify")
  public ResponseEntity<SuccessResponseDto<Boolean>> verifyJwtToken(
    @RequestBody String token
  ) {
    String username = jwtService.extractUserName(token);
    UserDetails userDetails = context
      .getBean(MongoUserDetailsService.class)
      .loadUserByUsername(username);

    boolean result = jwtService.validateToken(token, userDetails);
    return ResponseEntity
      .ok()
      .body(
        new SuccessResponseDto<Boolean>(
          result,
          result ? "Token is Valid" : "Token is Invalid"
        )
      );
  }
}
