package com.mdd.backend.Services.Subscription;

import com.mdd.backend.Exceptions.AlreadySubscribedException;
import com.mdd.backend.Exceptions.SubscriptionNotFoundException;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionRequest;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.SubscriptionRepository;
import com.mdd.backend.Services.Subject.SubjectService;
import com.mdd.backend.Services.User.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubjectService subjectService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public SubscriptionServiceImpl(
            SubscriptionRepository subscriptionRepository,
            SubjectService subjectService,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.subjectService = subjectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubscriptionResponse subscribe(SubscriptionRequest request) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        Subject subject = subjectService.getSubjectEntityById(request.getSubjectId());

        boolean alreadySubscribed = subscriptionRepository.existsByUserAndSubject(currentUser, subject);
        if (alreadySubscribed) {
            throw new AlreadySubscribedException("Vous êtes déjà abonné à ce sujet.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(currentUser);
        subscription.setSubject(subject);

        Subscription saved = subscriptionRepository.save(subscription);
        return convertEntityToDto(saved);
    }

    @Override
    public void unsubscribe(Long subjectId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        Subject subject = subjectService.getSubjectEntityById(subjectId);


        Subscription subscription = subscriptionRepository.findByUserAndSubject(currentUser, subject)
                .orElseThrow(() -> new SubscriptionNotFoundException("Aucun abonnement trouvé pour cet utilisateur et ce sujet."));

        subscriptionRepository.delete(subscription);
    }

    @Override
    public List<SubscriptionResponse> getSubscriptions() {
        User currentUser = userService.getCurrentAuthenticatedUser();

        return subscriptionRepository.findByUser(currentUser).stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSubscribed(Long subjectId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        Subject subject = subjectService.getSubjectEntityById(subjectId);


        return subscriptionRepository.existsByUserAndSubject(currentUser, subject);
    }

    @Override
    public SubscriptionResponse convertEntityToDto(Subscription subscription) {

        if (subscription == null) {
            throw new IllegalArgumentException("L'entité Subscription ne peut pas être nulle.");
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(subscription, SubscriptionResponse.class);
    }

    @Override
    public Subscription convertDtoToEntity(SubscriptionRequest subscriptionRequest) {
        return modelMapper.map(subscriptionRequest, Subscription.class);
    }
}
