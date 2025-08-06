package com.company.performance.config;

import com.company.performance.entity.User;
import com.company.performance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        // Create default admin user if not exists
        if (!userRepository.existsByEmail("admin@company.com")) {
            User adminUser = new User();
            adminUser.setEmail("admin@company.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setEmployeeId("ADMIN001");
            adminUser.setRole(User.Role.ADMIN);
            adminUser.setIsActive(true);
            adminUser.setJobTitle("System Administrator");
            adminUser.setDepartment("IT");
            adminUser.setHireDate(LocalDateTime.now());

            userRepository.save(adminUser);
            log.info("✅ Default admin user created: admin@company.com / admin123");
        }

        // Create default manager user if not exists
        if (!userRepository.existsByEmail("manager@company.com")) {
            User managerUser = new User();
            managerUser.setEmail("manager@company.com");
            managerUser.setPassword(passwordEncoder.encode("manager123"));
            managerUser.setFirstName("John");
            managerUser.setLastName("Manager");
            managerUser.setEmployeeId("MGR001");
            managerUser.setRole(User.Role.MANAGER);
            managerUser.setIsActive(true);
            managerUser.setJobTitle("Team Manager");
            managerUser.setDepartment("Engineering");
            managerUser.setHireDate(LocalDateTime.now());

            userRepository.save(managerUser);
            log.info("✅ Default manager user created: manager@company.com / manager123");
        }

        // Create default employee user if not exists
        if (!userRepository.existsByEmail("employee@company.com")) {
            User employeeUser = new User();
            employeeUser.setEmail("employee@company.com");
            employeeUser.setPassword(passwordEncoder.encode("employee123"));
            employeeUser.setFirstName("Jane");
            employeeUser.setLastName("Employee");
            employeeUser.setEmployeeId("EMP001");
            employeeUser.setRole(User.Role.EMPLOYEE);
            employeeUser.setIsActive(true);
            employeeUser.setJobTitle("Software Developer");
            employeeUser.setDepartment("Engineering");
            employeeUser.setHireDate(LocalDateTime.now());

            userRepository.save(employeeUser);
            log.info("✅ Default employee user created: employee@company.com / employee123");
        }

        log.info("🔧 Data initialization completed");
    }
}