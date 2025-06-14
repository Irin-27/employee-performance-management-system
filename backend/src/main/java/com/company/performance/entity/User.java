package com.company.performance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "employee_id", unique = true)
  private String employeeId;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "job_title")
  private String jobTitle;

  @Column(name = "department")
  private String department;

  @Column(name = "hire_date")
  private LocalDateTime hireDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @Column(name = "manager_id")
  private Long managerId;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Relationships will be added later when we create PerformanceReview and Goal
  // entities

  // Enum for user roles
  public enum Role {
    ADMIN, MANAGER, EMPLOYEE
  }

  // Helper methods
  public String getFullName() {
    return firstName + " " + lastName;
  }

  public boolean isManager() {
    return role == Role.MANAGER;
  }

  public boolean isAdmin() {
    return role == Role.ADMIN;
  }
}