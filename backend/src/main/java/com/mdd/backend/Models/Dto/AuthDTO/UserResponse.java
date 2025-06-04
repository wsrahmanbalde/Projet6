package com.mdd.backend.Models.Dto.AuthDTO;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private Role role;
    private List<SubjectResponse> subscriptions;
}
