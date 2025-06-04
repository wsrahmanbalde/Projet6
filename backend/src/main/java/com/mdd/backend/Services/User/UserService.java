package com.mdd.backend.Services.User;

import com.mdd.backend.Models.Dto.AuthDTO.UserRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserResponse;
import com.mdd.backend.Models.Dto.AuthDTO.UserWithSubscriptionsDTO;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionRequest;
import com.mdd.backend.Models.Dto.SubscriptionDTO.SubscriptionResponse;
import com.mdd.backend.Models.Subscription;
import com.mdd.backend.Models.User;

import java.util.Optional;

public interface UserService {
    UserResponse register(UserRequest user);
    Optional<User> findByEmailOrUsername(String emailOrUsername);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, User updatedUser);
    User getCurrentAuthenticatedUser();
    UserResponse updateUser(Long id, UserRequest updatedUser);

    UserResponse convertEntityToDto (User user);
    User convertDtoToEntity(UserRequest userRequest);
    UserWithSubscriptionsDTO convertToUserWithSubscriptions(User user);
}
