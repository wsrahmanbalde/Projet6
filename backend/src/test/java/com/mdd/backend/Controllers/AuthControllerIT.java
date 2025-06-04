package com.mdd.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdd.backend.Models.Auth.LoginRequest;
import com.mdd.backend.Models.Auth.RegisterRequest;
import com.mdd.backend.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterAndLogin_shouldSucceed() throws Exception {
        // --- Création d’un RegisterRequest (record) ---
        RegisterRequest registerRequest = new RegisterRequest(
                "integrationUser",
                "integration@example.com",
                "Password123!"
        );

        String registerJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // --- Vérifie que l'utilisateur a bien été enregistré ---
        assert userRepository.findByEmail("integration@example.com").isPresent();

        // --- Création d’un LoginRequest (classe Java classique) ---
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("integration@example.com");
        loginRequest.setPassword("Password123!");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}