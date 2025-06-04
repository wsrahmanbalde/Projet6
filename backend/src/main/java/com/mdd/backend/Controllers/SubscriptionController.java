package com.mdd.backend.Controllers;

import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionRequest;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionResponse;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.User;
import com.mdd.backend.Services.Subject.SubjectService;
import com.mdd.backend.Services.Subscription.SubscriptionService;
import com.mdd.backend.Services.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final SubjectService subjectService;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  UserService userService,
                                  SubjectService subjectService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @PostMapping("/{subjectId}")
    public ResponseEntity<SubscriptionResponse> subscribe(@PathVariable Long subjectId) {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setSubjectId(subjectId);
        SubscriptionResponse response = subscriptionService.subscribe(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long subjectId) {
        subscriptionService.unsubscribe(subjectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {
        List<SubscriptionResponse> subscriptions = subscriptionService.getSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/check/{subjectId}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable Long subjectId) {
        boolean subscribed = subscriptionService.isSubscribed(subjectId);
        return ResponseEntity.ok(subscribed);
    }
}
