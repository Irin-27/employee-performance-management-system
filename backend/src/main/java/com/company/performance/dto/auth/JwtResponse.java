package com.company.performance.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private Long expiresIn;
  private UserInfo user;

  public JwtResponse(String accessToken, String refreshToken, Long expiresIn, UserInfo user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.user = user;
  }
}