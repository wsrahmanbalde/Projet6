package com.mdd.backend.Repositories;

import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
    Optional<Subscription> findByUserAndSubject(User user, Subject subject);
    boolean existsByUserAndSubject(User user, Subject subject);
    void deleteByUserAndSubject(User user, Subject subject);
}
