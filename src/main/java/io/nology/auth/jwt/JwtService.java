package io.nology.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public Claims extractAllClaims(String token) {
    Claims allClaims = Jwts
      .parserBuilder()
      .setSigningKey(this.getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();

    return allClaims;
  }

  public Long extractUserId(String token) {
    Claims allClaims = this.extractAllClaims(token);
    return Long.parseLong(allClaims.getSubject());
  }

  public Date extractExpiration(String token) {
    Claims allClaims = this.extractAllClaims(token);
    return allClaims.getExpiration();
  }

  public boolean isTokenValid(String token, User user) {
    // check that token is not expired
    boolean idEqualsUserId = this.extractUserId(token).equals(user.getId().toString());

    boolean isTokenExpired = this.extractExpiration(token).before(new Date());

    if(idEqualsUserId && !isTokenExpired) {
      return true;
    }
    return false;
  }
}
