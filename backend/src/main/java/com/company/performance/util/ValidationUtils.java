package com.company.performance.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations
 */
public final class ValidationUtils {

  private ValidationUtils() {
    // Prevent instantiation
  }

  // Email validation pattern
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  // Password validation pattern (at least one letter, one number, minimum 6
  // characters)
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$");

  // Employee ID pattern (alphanumeric, 3-20 characters)
  private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile(
      "^[A-Za-z0-9]{3,20}$");

  // Phone number pattern (basic international format)
  private static final Pattern PHONE_PATTERN = Pattern.compile(
      "^[+]?[1-9]\\d{1,14}$");

  /**
   * Validate email address format
   */
  public static boolean isValidEmail(String email) {
    return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
  }

  /**
   * Validate password strength
   */
  public static boolean isValidPassword(String password) {
    return StringUtils.hasText(password) &&
        password.length() >= AppConstants.Validation.MIN_PASSWORD_LENGTH &&
        password.length() <= AppConstants.Validation.MAX_PASSWORD_LENGTH &&
        PASSWORD_PATTERN.matcher(password).matches();
  }

  /**
   * Validate employee ID format
   */
  public static boolean isValidEmployeeId(String employeeId) {
    return StringUtils.hasText(employeeId) &&
        EMPLOYEE_ID_PATTERN.matcher(employeeId.trim()).matches();
  }

  /**
   * Validate phone number format
   */
  public static boolean isValidPhoneNumber(String phoneNumber) {
    return StringUtils.hasText(phoneNumber) &&
        PHONE_PATTERN.matcher(phoneNumber.replaceAll("\\s+", "")).matches();
  }

  /**
   * Validate name (first name, last name)
   */
  public static boolean isValidName(String name) {
    return StringUtils.hasText(name) &&
        name.trim().length() >= 2 &&
        name.trim().length() <= AppConstants.Validation.MAX_NAME_LENGTH &&
        name.matches("^[a-zA-Z\\s'-]+$");
  }

  /**
   * Validate text length
   */
  public static boolean isValidTextLength(String text, int minLength, int maxLength) {
    if (!StringUtils.hasText(text)) {
      return minLength == 0;
    }
    int length = text.trim().length();
    return length >= minLength && length <= maxLength;
  }

  /**
   * Validate rating value
   */
  public static boolean isValidRating(Integer rating) {
    return rating != null &&
        rating >= AppConstants.PerformanceMetrics.MIN_RATING &&
        rating <= AppConstants.PerformanceMetrics.MAX_RATING;
  }

  /**
   * Validate date range
   */
  public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      return false;
    }
    return !startDate.isAfter(endDate);
  }

  /**
   * Validate date range with current date
   */
  public static boolean isValidDateRangeWithCurrent(LocalDate startDate, LocalDate endDate) {
    LocalDate today = LocalDate.now();
    return isValidDateRange(startDate, endDate) &&
        !endDate.isBefore(today);
  }

  /**
   * Validate user role
   */
  public static boolean isValidRole(String role) {
    return StringUtils.hasText(role) &&
        AppConstants.Roles.ALL_ROLES.contains(role.toUpperCase());
  }

  /**
   * Validate review status
   */
  public static boolean isValidReviewStatus(String status) {
    return StringUtils.hasText(status) &&
        AppConstants.ReviewStatus.ALL_STATUSES.contains(status.toUpperCase());
  }

  /**
   * Validate goal status
   */
  public static boolean isValidGoalStatus(String status) {
    return StringUtils.hasText(status) &&
        AppConstants.GoalStatus.ALL_STATUSES.contains(status.toUpperCase());
  }

  /**
   * Validate priority level
   */
  public static boolean isValidPriority(String priority) {
    return StringUtils.hasText(priority) &&
        AppConstants.Priority.ALL_PRIORITIES.contains(priority.toUpperCase());
  }

  /**
   * Validate file extension
   */
  public static boolean isValidFileExtension(String fileName) {
    if (!StringUtils.hasText(fileName)) {
      return false;
    }
    String extension = getFileExtension(fileName);
    return AppConstants.FileUpload.ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
  }

  /**
   * Get file extension from filename
   */
  public static String getFileExtension(String fileName) {
    if (!StringUtils.hasText(fileName)) {
      return "";
    }
    int lastDotIndex = fileName.lastIndexOf('.');
    return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
  }

  /**
   * Validate file size
   */
  public static boolean isValidFileSize(long fileSize) {
    return fileSize > 0 && fileSize <= AppConstants.FileUpload.MAX_FILE_SIZE;
  }

  /**
   * Sanitize input string
   */
  public static String sanitizeInput(String input) {
    if (!StringUtils.hasText(input)) {
      return "";
    }
    return input.trim()
        .replaceAll("\\s+", " ") // Replace multiple spaces with single space
        .replaceAll("[<>\"'&]", ""); // Remove potentially dangerous characters
  }

  /**
   * Validate percentage value
   */
  public static boolean isValidPercentage(Double percentage) {
    return percentage != null && percentage >= 0.0 && percentage <= 100.0;
  }

  /**
   * Check if date is in the future
   */
  public static boolean isFutureDate(LocalDate date) {
    return date != null && date.isAfter(LocalDate.now());
  }

  /**
   * Check if datetime is in the future
   */
  public static boolean isFutureDateTime(LocalDateTime dateTime) {
    return dateTime != null && dateTime.isAfter(LocalDateTime.now());
  }

  /**
   * Validate business email (not personal email domains)
   */
  public static boolean isBusinessEmail(String email) {
    if (!isValidEmail(email)) {
      return false;
    }

    String[] personalDomains = {
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com",
        "aol.com", "icloud.com", "live.com", "msn.com"
    };

    String domain = email.substring(email.lastIndexOf('@') + 1).toLowerCase();

    for (String personalDomain : personalDomains) {
      if (domain.equals(personalDomain)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Generate password strength score (0-5)
   */
  public static int getPasswordStrength(String password) {
    if (!StringUtils.hasText(password)) {
      return 0;
    }

    int score = 0;

    // Length
    if (password.length() >= 8)
      score++;
    if (password.length() >= 12)
      score++;

    // Character types
    if (password.matches(".*[a-z].*"))
      score++; // lowercase
    if (password.matches(".*[A-Z].*"))
      score++; // uppercase
    if (password.matches(".*\\d.*"))
      score++; // numbers
    if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
      score++; // special chars

    return Math.min(score, 5);
  }
}