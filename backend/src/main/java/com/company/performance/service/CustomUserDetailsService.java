package com.company.performance.service;

import com.company.performance.entity.User;
import com.company.performance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.debug("Loading user details for email: {}", email);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.error("User not found with email: {}", email);
          return new UsernameNotFoundException("User not found with email: " + email);
        });

    if (!user.getIsActive()) {
      log.error("User account is inactive: {}", email);
      throw new UsernameNotFoundException("User account is inactive: " + email);
    }

    log.debug("User loaded successfully: {}", email);
    return createUserPrincipal(user);
  }

  /**
   * Load user by ID (useful for JWT token validation)
   */
  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    log.debug("Loading user details for ID: {}", id);

    User user = userRepository.findById(id)
        .orElseThrow(() -> {
          log.error("User not found with ID: {}", id);
          return new UsernameNotFoundException("User not found with ID: " + id);
        });

    if (!user.getIsActive()) {
      log.error("User account is inactive: {}", user.getEmail());
      throw new UsernameNotFoundException("User account is inactive: " + user.getEmail());
    }

    return createUserPrincipal(user);
  }

  /**
   * Create Spring Security UserDetails from our User entity
   */
  private UserDetails createUserPrincipal(User user) {
    Collection<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .accountExpired(false)
        .accountLocked(!user.getIsActive())
        .credentialsExpired(false)
        .disabled(!user.getIsActive())
        .build();
  }

  /**
   * Get User entity from email (useful for additional user information)
   */
  @Transactional(readOnly = true)
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  /**
   * Get User entity from ID
   */
  @Transactional(readOnly = true)
  public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
  }
}