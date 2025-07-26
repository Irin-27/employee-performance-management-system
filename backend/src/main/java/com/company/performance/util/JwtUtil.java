package com.company.performance.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.expiration}")
  private long jwtExpirationMs;

  @Value("${app.jwt.refresh-expiration}")
  private long refreshExpirationMs;

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  /**
   * Generate JWT token for user authentication
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(), jwtExpirationMs);
  }

  /**
   * Generate refresh token
   */
  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("type", "refresh");
    return createToken(claims, userDetails.getUsername(), refreshExpirationMs);
  }

  /**
   * Create JWT token with claims and expiration
   */
  private String createToken(Map<String, Object> claims, String subject, long expiration) {
    Instant now = Instant.now();
    Instant expiryDate = now.plus(expiration, ChronoUnit.MILLIS);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiryDate))
        .signWith(getSigningKey(), Jwts.SIG.HS512)
        .compact();
  }

  /**
   * Extract username from JWT token
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extract expiration date from JWT token
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extract specific claim from JWT token
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extract all claims from JWT token
   */
  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException e) {
      log.error("Failed to parse JWT token: {}", e.getMessage());
      throw e;
    }
  }

  /**
   * Get signing key for JWT token
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Check if JWT token is expired
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Validate JWT token
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } catch (JwtException e) {
      log.error("JWT token validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Validate JWT token without UserDetails
   */
  public Boolean validateToken(String token) {
    try {
      extractAllClaims(token);
      return !isTokenExpired(token);
    } catch (JwtException e) {
      log.error("JWT token validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Extract JWT token from HTTP request
   */
  public String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  /**
   * Check if token is refresh token
   */
  public Boolean isRefreshToken(String token) {
    try {
      Claims claims = extractAllClaims(token);
      return "refresh".equals(claims.get("type"));
    } catch (JwtException e) {
      return false;
    }
  }

  /**
   * Get token expiration time in milliseconds
   */
  public long getExpirationTime() {
    return jwtExpirationMs;
  }

  /**
   * Get refresh token expiration time in milliseconds
   */
  public long getRefreshExpirationTime() {
    return refreshExpirationMs;
  }
}