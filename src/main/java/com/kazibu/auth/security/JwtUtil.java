package com.kazibu.auth.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
  private final String SECRET_KEY = "your_secret_key";
  private final long EXPIRATION = 86400000; // 1天

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }
}
