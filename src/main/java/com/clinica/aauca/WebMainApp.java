package com.clinica.aauca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Spring Boot Web version of Clínica Aauca.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.clinica.aauca")
public class WebMainApp {
    public static void main(String[] args) {
        SpringApplication.run(WebMainApp.class, args);
    }
}
