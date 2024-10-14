package io.nology.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.nology.auth.user.User;

@Component
public class JwtService {

  @Autowired
  private Dotenv dotenv;

  public String generateToken(User user) {
    return Jwts
      .builder()
      .setClaims(null)
      .setSubject(user.getId().toString())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
      .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key getSignInKey() {
    // read secret key from .env
    String secret = dotenv.get("JWT_SECRET");
    return null;
  }
}
