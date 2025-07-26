package com.company.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class PerformanceApplication {

	public static void main(String[] args) {
		log.info("Starting Employee Performance Management System...");

		try {
			SpringApplication app = new SpringApplication(PerformanceApplication.class);

			// Add additional configuration if needed
			// app.setAdditionalProfiles("dev"); // for development profile

			app.run(args);

			log.info("Employee Performance Management System started successfully!");
			log.info("API Documentation available at: http://localhost:8080/swagger-ui.html");
			log.info("Health Check available at: http://localhost:8080/actuator/health");
			log.info("Test Endpoints available at: http://localhost:8080/api/test/");

		} catch (Exception e) {
			log.error("Failed to start Employee Performance Management System", e);
			System.exit(1);
		}
	}
}