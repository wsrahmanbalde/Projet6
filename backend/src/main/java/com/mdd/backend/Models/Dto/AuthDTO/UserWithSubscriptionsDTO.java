package com.mdd.backend.Models.Dto.AuthDTO;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserWithSubscriptionsDTO {
    private Long id;
    private String email;
    private String username;
    private Role role;
    private List<SubjectResponse> subscriptions;
}