package com.company.performance.controller;

import com.company.performance.entity.User;
import com.company.performance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:5173")
public class TestController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("Hello from Employee Performance Management System!");
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }

  @PostMapping("/create-sample-user")
  public ResponseEntity<User> createSampleUser() {
    User user = new User();
    user.setEmail("john.doe@company.com");
    user.setPassword(passwordEncoder.encode("password123")); // Now properly encrypted
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmployeeId("EMP001");
    user.setJobTitle("Software Developer");
    user.setDepartment("IT");
    user.setRole(User.Role.EMPLOYEE);
    user.setHireDate(LocalDateTime.now());
    user.setIsActive(true);

    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
  }

  @GetMapping("/database-status")
  public ResponseEntity<String> checkDatabaseConnection() {
    try {
      long userCount = userRepository.count();
      return ResponseEntity.ok("Database connected successfully! Total users: " + userCount);
    } catch (Exception e) {
      return ResponseEntity.ok("Database connection failed: " + e.getMessage());
    }
  }
}