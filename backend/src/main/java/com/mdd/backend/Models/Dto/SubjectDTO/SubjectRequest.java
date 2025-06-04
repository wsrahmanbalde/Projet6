package com.mdd.backend.Models.Dto.SubjectDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    private String name;
    private String description;
}
