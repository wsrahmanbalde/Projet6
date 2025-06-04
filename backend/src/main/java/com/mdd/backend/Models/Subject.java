package com.mdd.backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String description;

    @OneToMany(mappedBy = "subject")
    private List<Post> posts;

    @OneToMany(mappedBy = "subject")
    private List<Subscription> subscriptions;
}
