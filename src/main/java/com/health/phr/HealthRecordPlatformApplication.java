package com.health.phr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. Each direct sub-package is a Spring Modulith application module;
 * {@code ModularityTests} fails the build if modules reach into each other's internals.
 */
public class HealthRecordPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthRecordPlatformApplication.class, args);
    }
}
