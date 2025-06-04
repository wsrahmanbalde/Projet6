package com.mdd.backend.Models.Dto.CommentDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private Long postId;
    private LocalDateTime createdAt;
}