package com.mdd.backend.Services;

import com.mdd.backend.Exceptions.AlreadySubscribedException;
import com.mdd.backend.Exceptions.SubscriptionNotFoundException;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionRequest;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.SubscriptionRepository;
import com.mdd.backend.Services.Subject.SubjectService;
import com.mdd.backend.Services.Subscription.SubscriptionServiceImpl;
import com.mdd.backend.Services.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    private SubscriptionRepository subscriptionRepository;
    private SubjectService subjectService;
    private UserService userService;

    private SubscriptionServiceImpl subscriptionService;

    private User mockUser;
    private Subject mockSubject;
    private Subscription mockSubscription;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        subjectService = mock(SubjectService.class);
        userService = mock(UserService.class);
        ModelMapper modelMapper = new ModelMapper();

        subscriptionService = new SubscriptionServiceImpl(
                subscriptionRepository, subjectService, userService, modelMapper
        );

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        mockSubject = new Subject();
        mockSubject.setId(10L);
        mockSubject.setName("Java");

        mockSubscription = new Subscription();
        mockSubscription.setId(100L);
        mockSubscription.setUser(mockUser);
        mockSubscription.setSubject(mockSubject);
    }

    @Test
    void subscribe_ShouldReturnSubscriptionResponse_WhenNotAlreadySubscribed() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setSubjectId(mockSubject.getId());

        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subjectService.getSubjectEntityById(mockSubject.getId())).thenReturn(mockSubject);
        when(subscriptionRepository.existsByUserAndSubject(mockUser, mockSubject)).thenReturn(false);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(mockSubscription);

        SubscriptionResponse response = subscriptionService.subscribe(request);

        assertNotNull(response);
        assertEquals(mockSubject.getName(), response.getSubjectName());
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void subscribe_ShouldThrowException_WhenAlreadySubscribed() {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setSubjectId(mockSubject.getId());

        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subjectService.getSubjectEntityById(mockSubject.getId())).thenReturn(mockSubject);
        when(subscriptionRepository.existsByUserAndSubject(mockUser, mockSubject)).thenReturn(true);

        assertThrows(AlreadySubscribedException.class, () -> subscriptionService.subscribe(request));
    }

    @Test
    void unsubscribe_ShouldDeleteSubscription_WhenSubscriptionExists() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subjectService.getSubjectEntityById(mockSubject.getId())).thenReturn(mockSubject);
        when(subscriptionRepository.findByUserAndSubject(mockUser, mockSubject))
                .thenReturn(Optional.of(mockSubscription));

        assertDoesNotThrow(() -> subscriptionService.unsubscribe(mockSubject.getId()));

        verify(subscriptionRepository).delete(mockSubscription);
    }

    @Test
    void unsubscribe_ShouldThrowException_WhenSubscriptionNotFound() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subjectService.getSubjectEntityById(mockSubject.getId())).thenReturn(mockSubject);
        when(subscriptionRepository.findByUserAndSubject(mockUser, mockSubject))
                .thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.unsubscribe(mockSubject.getId()));
    }

    @Test
    void getSubscriptions_ShouldReturnListOfSubscriptionResponse() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subscriptionRepository.findByUser(mockUser)).thenReturn(List.of(mockSubscription));

        List<SubscriptionResponse> responses = subscriptionService.getSubscriptions();

        assertEquals(1, responses.size());
        assertEquals(mockSubject.getName(), responses.get(0).getSubjectName());
    }

    @Test
    void isSubscribed_ShouldReturnTrue_WhenSubscribed() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(mockUser);
        when(subjectService.getSubjectEntityById(mockSubject.getId())).thenReturn(mockSubject);
        when(subscriptionRepository.existsByUserAndSubject(mockUser, mockSubject)).thenReturn(true);

        assertTrue(subscriptionService.isSubscribed(mockSubject.getId()));
    }

    @Test
    void convertEntityToDto_ShouldMapCorrectly() {
        SubscriptionResponse response = subscriptionService.convertEntityToDto(mockSubscription);

        assertNotNull(response);
        assertEquals(mockUser.getUsername(), response.getUsername());
        assertEquals(mockSubject.getName(), response.getSubjectName());
    }

    @Test
    void convertEntityToDto_ShouldThrowException_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> subscriptionService.convertEntityToDto(null));
    }
}
