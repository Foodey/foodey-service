package com.foodey.server.security;

import com.foodey.server.auth.enums.TokenType;
import com.foodey.server.auth.model.RefreshToken;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;

  @Value("${foodey.jwt.secret-key}")
  private String JWT_SECRET_KEY;

  @Value("${foodey.jwt.access-token-expiration}")
  private long ACCESS_TOKEN_EXPIRATION;

  @Value("${foodey.jwt.refresh-token-expiration}")
  private long REFRESH_TOKEN_EXPIRATION;

  public String generateAccessToken(User user) {
    return buildToken(user.getPubId(), ACCESS_TOKEN_EXPIRATION);
  }

  public String generateAccessToken(String subject) {
    return buildToken(subject, ACCESS_TOKEN_EXPIRATION);
  }

  public boolean isAccessTokenValid(String token, User user) {
    return isTokenValid(token, user.getPubId());
  }

  public RefreshToken generateRefreshToken(User user) {
    return new RefreshToken(user.getPubId(), REFRESH_TOKEN_EXPIRATION);
  }

  public boolean isTokenValid(String token, String subject) {
    return subject.equals(extractSubject(token)) && !isTokenExpired(token);
  }

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String buildToken(String subject, long expirationMsFromNow) {
    return buildToken(new HashMap<>(), subject, expirationMsFromNow);
  }

  public String buildToken(String subject, Date expiration) {
    return buildToken(new HashMap<>(), subject, expiration);
  }

  public String buildToken(
      Map<String, Object> extraClaims, String subject, long expirationMsFromNow) {
    return buildToken(
        extraClaims, subject, new Date(System.currentTimeMillis() + expirationMsFromNow));
  }

  public String buildToken(Map<String, Object> extraClaims, String subject, Date expiration) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(expiration)
        .signWith(getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
      throw new InvalidTokenRequestException(TokenType.BEARER, token, "Malformed jwt token");

    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
      throw new InvalidTokenRequestException(
          TokenType.BEARER, token, "Token expired. Refresh required");

    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
      throw new InvalidTokenRequestException(TokenType.BEARER, token, "Unsupported JWT token");

    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
      throw new InvalidTokenRequestException(TokenType.BEARER, token, "Illegal argument token");

    } catch (JwtException ex) {
      log.error("Invalid JWT token");
      throw new InvalidTokenRequestException(TokenType.BEARER, token, "Invalid JWT token");
    }
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
