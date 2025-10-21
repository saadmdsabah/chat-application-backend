package com.saadsabahuddin.chat_application_backend.config;

import com.saadsabahuddin.chat_application_backend.filters.JwtFilter;
import com.saadsabahuddin.chat_application_backend.service.MongoUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final MongoUserDetailsService userDetailsService;
  private final JwtFilter jwtFilter;

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @SuppressWarnings("deprecation")
  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  AuthenticationManager authenticationManager(
    AuthenticationConfiguration config
  ) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(
    HttpSecurity http,
    AuthenticationProvider authProvider
  ) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> {})
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(
            "/auth/login",
            "/auth/register",
            "/token/verify",
            "/topic/**",
            "/app/**",
            "/chat/**"
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .authenticationProvider(authProvider)
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}
