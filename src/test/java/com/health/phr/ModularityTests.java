package com.health.phr;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Fails the build if one module uses another module's internal types or if modules depend on each other in a cycle.
 * Also writes module diagrams to target/spring-modulith-docs for the README.
 */
class ModularityTests {

    private final ApplicationModules modules = ApplicationModules.of(HealthRecordPlatformApplication.class);

    @Test
    void moduleBoundariesAreRespected() {
        modules.verify();
    }

    @Test
    void writesModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
