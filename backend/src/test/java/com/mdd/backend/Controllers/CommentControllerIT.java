package com.mdd.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdd.backend.Models.Dto.CommentDTO.CommentRequest;
import com.mdd.backend.Models.Dto.CommentDTO.CommentResponse;
import com.mdd.backend.Models.Post;
import com.mdd.backend.Models.Role;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.CommentRepository;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "test@example.com", roles = {"USER"})
class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private Long postId;
    private Long userId;

    @BeforeEach
    void setup() {
        // Nettoyer la base si besoin
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Création d’un utilisateur
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();
        userId = savedUser.getId();

        // Création d’un post avec l’auteur
        Post post = new Post();
        post.setTitle("Post de test");
        post.setContent("Contenu du post");
        post.setAuthor(savedUser);
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        assertThat(savedPost.getId()).isNotNull();
        postId = savedPost.getId();

        // Pour s'assurer que les données sont persistées avant les tests (utile parfois)
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @Test
    void create_get_delete_comment_shouldWork() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPostId(postId);
        commentRequest.setContent("Un commentaire de test");

        String commentJson = objectMapper.writeValueAsString(commentRequest);

        // Création du commentaire
        String responseJson = mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("Un commentaire de test"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);
        Long commentId = response.getId();

        // Récupération des commentaires par postId
        mockMvc.perform(get("/api/comments/post/" + postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentId))
                .andExpect(jsonPath("$[0].content").value("Un commentaire de test"));

        // Suppression du commentaire
        mockMvc.perform(delete("/api/comments/" + commentId))
                .andExpect(status().isNoContent());

        // Vérification de la suppression
        assertThat(commentRepository.findById(commentId)).isNotPresent();
    }
}