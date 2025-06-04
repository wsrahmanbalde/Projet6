package com.mdd.backend.Models.Dto.PostDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String subjectName; // Ou subjectId, ou SubjectDTO
    private String authorUsername;
    private LocalDateTime createdAt;
}
