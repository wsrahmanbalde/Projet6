package com.mdd.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"})
class SubjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubjectRepository subjectRepository;

    private Long subjectId;
    private final String subjectName = "Programmation";
    private final String subjectDescription = "Une description obligatoire";

    @BeforeEach
    void setup() {
        // Nettoyer la base avant chaque test
        subjectRepository.deleteAll();

        // Créer un sujet pour les tests GET
        Subject subject = new Subject();
        subject.setName(subjectName);
        subject.setDescription(subjectDescription);
        Subject savedSubject = subjectRepository.save(subject);
        subjectId = savedSubject.getId();

        assertThat(subjectId).isNotNull();
    }

    @Test
    void createAndGetSubjects_shouldWork() throws Exception {
        // Créer un nouveau sujet avec un nom unique
        String dynamicName = "Développement Web " + System.currentTimeMillis();

        SubjectRequest newSubject = new SubjectRequest();
        newSubject.setName(dynamicName);
        newSubject.setDescription("Description pour Développement Web");

        String requestJson = objectMapper.writeValueAsString(newSubject);

        String responseJson = mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk()) // ou isCreated() si ton contrôleur renvoie 201
                .andExpect(jsonPath("$.name", startsWith("Développement Web")))
                .andExpect(jsonPath("$.description").value("Description pour Développement Web"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        SubjectResponse createdSubject = objectMapper.readValue(responseJson, SubjectResponse.class);
        assertThat(createdSubject.getId()).isNotNull();
        assertThat(createdSubject.getName()).startsWith("Développement Web");
        assertThat(createdSubject.getDescription()).isEqualTo("Description pour Développement Web");

        // Récupérer tous les sujets
        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.name == '" + subjectName + "')]").exists())
                .andExpect(jsonPath("$[?(@.name == '" + dynamicName + "')]").exists());

        // Récupérer sujet par id
        mockMvc.perform(get("/api/subjects/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subjectId))
                .andExpect(jsonPath("$.name").value(subjectName))
                .andExpect(jsonPath("$.description").value(subjectDescription));

        // Récupérer sujet par name
        mockMvc.perform(get("/api/subjects/name/" + subjectName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(subjectId))
                .andExpect(jsonPath("$.name").value(subjectName))
                .andExpect(jsonPath("$.description").value(subjectDescription));
    }
}