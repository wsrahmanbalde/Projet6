package com.mdd.backend.Services.Subscription;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionRequest;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.User;

import java.util.List;

public interface SubscriptionService {

    SubscriptionResponse subscribe(SubscriptionRequest request); // Utilise uniquement le subjectId ou subjectName

    void unsubscribe(Long subjectId); // L'utilisateur est extrait depuis la session

    List<SubscriptionResponse> getSubscriptions(); // Récupère l'utilisateur connecté automatiquement

    boolean isSubscribed(Long subjectId); // Vérifie l'abonnement de l'utilisateur connecté à un sujet donné

    SubscriptionResponse convertEntityToDto (Subscription subscription);
    Subscription convertDtoToEntity(SubscriptionRequest subscriptionRequest);
}