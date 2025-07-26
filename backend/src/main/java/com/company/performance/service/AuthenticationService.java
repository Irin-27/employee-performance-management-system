package com.company.performance.service;

import com.company.performance.dto.auth.*;
import com.company.performance.entity.User;
import com.company.performance.repository.UserRepository;
import com.company.performance.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService userDetailsService;

  /**
   * Authenticate user and generate JWT tokens
   */
  @Transactional
  public JwtResponse authenticateUser(LoginRequest loginRequest) {
    try {
      log.info("Attempting to authenticate user: {}", loginRequest.getEmail());

      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(),
              loginRequest.getPassword()));

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      User user = userDetailsService.getUserByEmail(userDetails.getUsername());

      // Generate tokens
      String accessToken = jwtUtil.generateToken(userDetails);
      String refreshToken = jwtUtil.generateRefreshToken(userDetails);

      log.info("User authenticated successfully: {}", loginRequest.getEmail());

      return new JwtResponse(
          accessToken,
          refreshToken,
          jwtUtil.getExpirationTime(),
          mapToUserInfo(user));

    } catch (BadCredentialsException e) {
      log.error("Authentication failed for user {}: Invalid credentials", loginRequest.getEmail());
      throw new BadCredentialsException("Invalid email or password");
    } catch (DisabledException e) {
      log.error("Authentication failed for user {}: Account disabled", loginRequest.getEmail());
      throw new DisabledException("Account is disabled");
    } catch (AuthenticationException e) {
      log.error("Authentication failed for user {}: {}", loginRequest.getEmail(), e.getMessage());
      throw new BadCredentialsException("Authentication failed");
    }
  }

  /**
   * Register new user (Admin only functionality)
   */
  @Transactional
  public UserInfo registerUser(RegisterRequest registerRequest) {
    log.info("Attempting to register new user: {}", registerRequest.getEmail());

    // Check if email already exists
    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new RuntimeException("Email is already in use");
    }

    // Check if employee ID already exists (if provided)
    if (registerRequest.getEmployeeId() != null &&
        userRepository.existsByEmployeeId(registerRequest.getEmployeeId())) {
      throw new RuntimeException("Employee ID is already in use");
    }

    // Create new user
    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setFirstName(registerRequest.getFirstName());
    user.setLastName(registerRequest.getLastName());
    user.setEmployeeId(registerRequest.getEmployeeId());
    user.setPhoneNumber(registerRequest.getPhoneNumber());
    user.setJobTitle(registerRequest.getJobTitle());
    user.setDepartment(registerRequest.getDepartment());
    user.setManagerId(registerRequest.getManagerId());
    user.setRole(User.Role.EMPLOYEE); // Default role
    user.setIsActive(true);
    user.setHireDate(LocalDateTime.now());

    User savedUser = userRepository.save(user);
    log.info("User registered successfully: {}", savedUser.getEmail());

    return mapToUserInfo(savedUser);
  }

  /**
   * Refresh JWT access token
   */
  public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
    try {
      String refreshToken = refreshTokenRequest.getRefreshToken();

      if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
        throw new RuntimeException("Invalid refresh token");
      }

      String username = jwtUtil.extractUsername(refreshToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      User user = userDetailsService.getUserByEmail(username);

      // Generate new access token
      String newAccessToken = jwtUtil.generateToken(userDetails);
      String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

      log.info("Token refreshed successfully for user: {}", username);

      return new JwtResponse(
          newAccessToken,
          newRefreshToken,
          jwtUtil.getExpirationTime(),
          mapToUserInfo(user));

    } catch (Exception e) {
      log.error("Token refresh failed: {}", e.getMessage());
      throw new RuntimeException("Token refresh failed");
    }
  }

  /**
   * Change user password
   */
  @Transactional
  public void changePassword(String userEmail, ChangePasswordRequest changePasswordRequest) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Verify current password
    if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
      throw new RuntimeException("Current password is incorrect");
    }

    // Update password
    user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    userRepository.save(user);

    log.info("Password changed successfully for user: {}", userEmail);
  }

  /**
   * Get current user information
   */
  public UserInfo getCurrentUser(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return mapToUserInfo(user);
  }

  /**
   * Map User entity to UserInfo DTO
   */
  private UserInfo mapToUserInfo(User user) {
    return new UserInfo(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmployeeId(),
        user.getJobTitle(),
        user.getDepartment(),
        user.getRole().name(),
        user.getManagerId(),
        user.getIsActive());
  }

  /**
   * Validate user exists and is active
   */
  public boolean validateUser(String email) {
    return userRepository.findByEmail(email)
        .map(User::getIsActive)
        .orElse(false);
  }
}