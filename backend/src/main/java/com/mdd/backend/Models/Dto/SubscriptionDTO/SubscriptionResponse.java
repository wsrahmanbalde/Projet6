package com.mdd.backend.Models.Dto.SubscriptionDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private String username;
    private String subjectName;
    private String subjectDescription;
    private LocalDateTime subscriptionDate;
}
