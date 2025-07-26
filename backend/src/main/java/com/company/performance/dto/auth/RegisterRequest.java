package com.company.performance.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Please provide a valid email address")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
  private String password;

  @NotBlank(message = "First name is required")
  @Size(max = 50, message = "First name must not exceed 50 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(max = 50, message = "Last name must not exceed 50 characters")
  private String lastName;

  @Size(max = 20, message = "Employee ID must not exceed 20 characters")
  private String employeeId;

  @Size(max = 15, message = "Phone number must not exceed 15 characters")
  private String phoneNumber;

  @Size(max = 100, message = "Job title must not exceed 100 characters")
  private String jobTitle;

  @Size(max = 50, message = "Department must not exceed 50 characters")
  private String department;

  private Long managerId;
}