package com.mdd.backend.Repositories;

import com.mdd.backend.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(); // du plus récent au plus ancien
    List<Post> findAllByOrderByCreatedAtAsc();  // du plus ancien au plus récent
    List<Post> findBySubjectId(Long subjectId);
}
