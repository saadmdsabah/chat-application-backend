package com.saadsabahuddin.chat_application_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOriginPatterns("http://localhost:5173")
          .allowedMethods("*")
          .allowedHeaders("*")
          .allowCredentials(true);
      }
    };
  }
}
