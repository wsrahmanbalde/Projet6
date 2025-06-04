package com.mdd.backend.Models.Dto.AuthDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String username;
    private String password;
}
