package com.saadsabahuddin.chat_application_backend.utils;

import com.saadsabahuddin.chat_application_backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {

  private final String secretKey;

  public JwtUtils(@Value("${jwt.secret}") String secretKey) {
    this.secretKey = secretKey;
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts
      .builder()
      .setClaims(claims)
      .setSubject(user.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(
        new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8))
      )
      .signWith(getKey())
      .compact();
  }

  private Key getKey() {
    byte[] secretBytesArray = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(secretBytesArray);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parserBuilder()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (
      userName.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
