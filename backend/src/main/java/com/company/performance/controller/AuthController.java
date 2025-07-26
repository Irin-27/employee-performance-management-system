package com.company.performance.controller;

import com.company.performance.dto.auth.*;
import com.company.performance.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService authenticationService;

  /**
   * User login endpoint
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    log.info("Login attempt for user: {}", loginRequest.getEmail());

    try {
      JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);

      return ResponseEntity.ok(
          ApiResponse.success("Login successful", jwtResponse));
    } catch (Exception e) {
      log.error("Login failed for user {}: {}", loginRequest.getEmail(), e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * User registration endpoint (Admin only)
   */
  @PostMapping("/register")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
    log.info("Registration attempt for user: {}", registerRequest.getEmail());

    try {
      UserInfo userInfo = authenticationService.registerUser(registerRequest);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("User registered successfully", userInfo));
    } catch (Exception e) {
      log.error("Registration failed for user {}: {}", registerRequest.getEmail(), e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Refresh token endpoint
   */
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    log.info("Token refresh attempt");

    try {
      JwtResponse jwtResponse = authenticationService.refreshToken(refreshTokenRequest);

      return ResponseEntity.ok(
          ApiResponse.success("Token refreshed successfully", jwtResponse));
    } catch (Exception e) {
      log.error("Token refresh failed: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Get current user information
   */
  @GetMapping("/me")
  public ResponseEntity<ApiResponse> getCurrentUser(Authentication authentication) {
    try {
      String userEmail = authentication.getName();
      UserInfo userInfo = authenticationService.getCurrentUser(userEmail);

      return ResponseEntity.ok(
          ApiResponse.success("User information retrieved successfully", userInfo));
    } catch (Exception e) {
      log.error("Failed to get current user: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Change password endpoint
   */
  @PutMapping("/change-password")
  public ResponseEntity<ApiResponse> changePassword(
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
      Authentication authentication) {

    try {
      String userEmail = authentication.getName();
      authenticationService.changePassword(userEmail, changePasswordRequest);

      return ResponseEntity.ok(
          ApiResponse.success("Password changed successfully"));
    } catch (Exception e) {
      log.error("Password change failed: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Logout endpoint (client-side token invalidation)
   */
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse> logout() {
    // In JWT implementation, logout is typically handled client-side
    // by removing the token from storage. Server-side token blacklisting
    // can be implemented for enhanced security if needed.

    return ResponseEntity.ok(
        ApiResponse.success("Logged out successfully"));
  }

  /**
   * Health check for authentication service
   */
  @GetMapping("/health")
  public ResponseEntity<ApiResponse> health() {
    return ResponseEntity.ok(
        ApiResponse.success("Authentication service is running"));
  }
}