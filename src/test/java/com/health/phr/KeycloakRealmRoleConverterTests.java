package com.health.phr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRealmRoleConverterTests {

    private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter();

    @Test
    void mapsRealmRolesToSpringAuthorities() {
        Jwt jwt = jwtWithClaim("realm_access", Map.of("roles", List.of("DOCTOR", "offline_access")));

        assertThat(converter.convert(jwt))
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_DOCTOR", "ROLE_offline_access");
    }

    @Test
    void returnsNoAuthoritiesWhenTheClaimIsMissing() {
        Jwt jwt = jwtWithClaim("preferred_username", "someone");

        assertThat(converter.convert(jwt)).isEmpty();
    }

    @Test
    void ignoresAMalformedRolesClaim() {
        Jwt jwt = jwtWithClaim("realm_access", Map.of("roles", "DOCTOR"));

        assertThat(converter.convert(jwt)).isEmpty();
    }

    private static Jwt jwtWithClaim(String name, Object value) {
        return Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .subject("user-1")
            .claim(name, value)
            .build();
    }
}
