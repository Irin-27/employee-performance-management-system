package com.company.performance.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String employeeId;
  private String jobTitle;
  private String department;
  private String role;
  private Long managerId;
  private Boolean isActive;
}