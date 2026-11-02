package com.health.phr.identity;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.health.phr.SecurityConfig;

@WebMvcTest(MeController.class)
@Import(SecurityConfig.class)
class MeControllerTests {

    @Autowired
    MockMvc mvc;

    // Tokens are supplied by the jwt() post-processor, so the real decoder is never called.
    @MockitoBean
    JwtDecoder jwtDecoder;

    @Test
    void returnsTheCallerFromTheToken() throws Exception {
        mvc.perform(get("/api/v1/me").with(jwt()
                .jwt(token -> token.subject("kc-123").claim("preferred_username", "dr.rao"))
                .authorities(new SimpleGrantedAuthority("ROLE_DOCTOR"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.subject").value("kc-123"))
            .andExpect(jsonPath("$.username").value("dr.rao"))
            .andExpect(jsonPath("$.roles[0]").value("DOCTOR"));
    }

    @Test
    void rejectsACallWithoutAToken() throws Exception {
        mvc.perform(get("/api/v1/me"))
            .andExpect(status().isUnauthorized());
    }
}
