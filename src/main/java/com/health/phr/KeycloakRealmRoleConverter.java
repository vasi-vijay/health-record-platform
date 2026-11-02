package com.health.phr;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Reads Keycloak realm roles from the {@code realm_access.roles} claim and maps each to {@code ROLE_<name>}.
 * Missing or malformed claims produce no authorities, never an error.
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object realmAccess = jwt.getClaims().get("realm_access");
        if (!(realmAccess instanceof Map<?, ?> access)) {
            return List.of();
        }
        Object roles = access.get("roles");
        if (!(roles instanceof Collection<?> names)) {
            return List.of();
        }
        return names.stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .<GrantedAuthority>map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .toList();
    }
}
