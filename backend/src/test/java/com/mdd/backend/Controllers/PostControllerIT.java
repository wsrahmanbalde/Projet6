package com.mdd.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Role;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Repositories.SubjectRepository;
import com.mdd.backend.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@WithMockUser(username = "test@example.com", roles = {"USER"})
class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PostRepository postRepository;

    private Long userId;
    private Long subjectId;

    @BeforeEach
    void setup() {
        // Créer un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        user.setRole(Role.USER);
        userRepository.save(user);
        userId = user.getId();

        // Créer un sujet (Subject)
        Subject subject = new Subject();
        subject.setName("Sujet Test");
        subjectRepository.save(subject);
        subjectId = subject.getId();
    }

    @Test
    void create_getById_getAllAsc_getAllDesc_getBySubject_shouldWork() throws Exception {
        // 1. Création du post via API
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("Titre test");
        postRequest.setContent("Contenu test");
        postRequest.setSubjectId(subjectId);

        String postJson = objectMapper.writeValueAsString(postRequest);

        // POST /api/posts
        String responseJson = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Titre test"))
                .andExpect(jsonPath("$.content").value("Contenu test"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PostResponse postResponse = objectMapper.readValue(responseJson, PostResponse.class);
        Long postId = postResponse.getId();

        // 2. GET /api/posts/{id}
        mockMvc.perform(get("/api/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Titre test"));

        // 3. GET /api/posts/asc
        mockMvc.perform(get("/api/posts/asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.id == " + postId + ")]").exists());

        // 4. GET /api/posts/desc
        mockMvc.perform(get("/api/posts/desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.id == " + postId + ")]").exists());

        // 5. GET /api/posts/subject/{subjectId}
        mockMvc.perform(get("/api/posts/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(postId))
                .andExpect(jsonPath("$[0].title").value("Titre test"));
    }
}
