package com.company.performance.repository;

import com.company.performance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  // Find user by email for authentication
  Optional<User> findByEmail(String email);

  // Find user by employee ID
  Optional<User> findByEmployeeId(String employeeId);

  // Check if email exists
  boolean existsByEmail(String email);

  // Check if employee ID exists
  boolean existsByEmployeeId(String employeeId);

  // Find all active users
  List<User> findByIsActiveTrue();

  // Find users by role
  List<User> findByRole(User.Role role);

  // Find users by department
  List<User> findByDepartment(String department);

  // Find users by manager
  List<User> findByManagerId(Long managerId);

  // Find all managers
  @Query("SELECT u FROM User u WHERE u.role = 'MANAGER' AND u.isActive = true")
  List<User> findAllActiveManagers();

  // Find employees under a specific manager
  @Query("SELECT u FROM User u WHERE u.managerId = :managerId AND u.isActive = true")
  List<User> findEmployeesByManager(@Param("managerId") Long managerId);

  // Search users by name
  @Query("SELECT u FROM User u WHERE " +
      "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
      "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
      "u.isActive = true")
  List<User> searchUsersByName(@Param("searchTerm") String searchTerm);
}