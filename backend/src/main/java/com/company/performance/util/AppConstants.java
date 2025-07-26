package com.company.performance.util;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Application-wide constants and utility methods
 */
public final class AppConstants {

  private AppConstants() {
    // Prevent instantiation
  }

  // API Constants
  public static final String API_VERSION = "v1";
  public static final String API_BASE_PATH = "/api";

  // Security Constants
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String ROLE_PREFIX = "ROLE_";

  // JWT Constants
  public static final long JWT_EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 hours
  public static final long JWT_REFRESH_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

  // Date/Time Constants
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

  // Performance Review Constants
  public static final class ReviewStatus {
    public static final String DRAFT = "DRAFT";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String IN_REVIEW = "IN_REVIEW";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    public static final List<String> ALL_STATUSES = Arrays.asList(
        DRAFT, SUBMITTED, IN_REVIEW, APPROVED, REJECTED);
  }

  // Goal Constants
  public static final class GoalStatus {
    public static final String NOT_STARTED = "NOT_STARTED";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";
    public static final String OVERDUE = "OVERDUE";

    public static final List<String> ALL_STATUSES = Arrays.asList(
        NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELLED, OVERDUE);
  }

  // Priority Levels
  public static final class Priority {
    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";
    public static final String CRITICAL = "CRITICAL";

    public static final List<String> ALL_PRIORITIES = Arrays.asList(
        LOW, MEDIUM, HIGH, CRITICAL);
  }

  // User Roles
  public static final class Roles {
    public static final String ADMIN = "ADMIN";
    public static final String MANAGER = "MANAGER";
    public static final String EMPLOYEE = "EMPLOYEE";

    public static final List<String> ALL_ROLES = Arrays.asList(
        ADMIN, MANAGER, EMPLOYEE);

    // Role hierarchies
    public static final List<String> MANAGER_ROLES = Arrays.asList(MANAGER, ADMIN);
    public static final List<String> ADMIN_ROLES = Arrays.asList(ADMIN);
  }

  // Email Templates
  public static final class EmailTemplates {
    public static final String WELCOME = "welcome";
    public static final String PASSWORD_RESET = "password-reset";
    public static final String REVIEW_REMINDER = "review-reminder";
    public static final String GOAL_DEADLINE = "goal-deadline";
    public static final String REVIEW_SUBMITTED = "review-submitted";
    public static final String REVIEW_APPROVED = "review-approved";
  }

  // File Upload Constants
  public static final class FileUpload {
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        ".pdf", ".doc", ".docx", ".jpg", ".jpeg", ".png");
    public static final String UPLOAD_DIR = "uploads/";
  }

  // Pagination Constants
  public static final class Pagination {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";
  }

  // Validation Constants
  public static final class Validation {
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_COMMENT_LENGTH = 500;
  }

  // Performance Metrics
  public static final class PerformanceMetrics {
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final double EXCELLENT_THRESHOLD = 4.5;
    public static final double GOOD_THRESHOLD = 3.5;
    public static final double SATISFACTORY_THRESHOLD = 2.5;
  }

  // System Messages
  public static final class Messages {
    // Success Messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String PASSWORD_CHANGED = "Password changed successfully";
    public static final String PROFILE_UPDATED = "Profile updated successfully";
    public static final String REVIEW_SUBMITTED = "Performance review submitted successfully";
    public static final String GOAL_CREATED = "Goal created successfully";
    public static final String GOAL_UPDATED = "Goal updated successfully";

    // Error Messages
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String INVALID_TOKEN = "Invalid or expired token";
    public static final String EMAIL_ALREADY_EXISTS = "Email address already exists";
    public static final String EMPLOYEE_ID_EXISTS = "Employee ID already exists";
    public static final String REVIEW_NOT_FOUND = "Performance review not found";
    public static final String GOAL_NOT_FOUND = "Goal not found";
    public static final String INVALID_DATE_RANGE = "Invalid date range";
    public static final String FILE_UPLOAD_ERROR = "File upload failed";
    public static final String EMAIL_SEND_ERROR = "Failed to send email";
  }

  // Application Events
  public static final class Events {
    public static final String USER_REGISTERED = "user.registered";
    public static final String USER_LOGIN = "user.login";
    public static final String USER_LOGOUT = "user.logout";
    public static final String PASSWORD_CHANGED = "password.changed";
    public static final String REVIEW_SUBMITTED = "review.submitted";
    public static final String REVIEW_APPROVED = "review.approved";
    public static final String GOAL_CREATED = "goal.created";
    public static final String GOAL_COMPLETED = "goal.completed";
  }

  // Cache Keys
  public static final class CacheKeys {
    public static final String USER_CACHE = "users";
    public static final String ROLE_CACHE = "roles";
    public static final String DEPARTMENT_CACHE = "departments";
    public static final String PERFORMANCE_STATS = "performance-stats";
  }
}