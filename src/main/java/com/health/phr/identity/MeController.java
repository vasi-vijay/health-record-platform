package com.health.phr.identity;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Week 1 end-to-end check: returns who the caller is according to the Keycloak token.
 * Proves the login, token validation and role mapping work before any health data exists.
 */
@RestController
@RequestMapping("/api/v1/me")
class MeController {

    @GetMapping
    MeResponse me(@AuthenticationPrincipal Jwt jwt, Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> authority.startsWith("ROLE_"))
            .map(authority -> authority.substring("ROLE_".length()))
            .sorted()
            .toList();
        return new MeResponse(jwt.getSubject(), jwt.getClaimAsString("preferred_username"), roles);
    }

    record MeResponse(String subject, String username, List<String> roles) {
    }
}
