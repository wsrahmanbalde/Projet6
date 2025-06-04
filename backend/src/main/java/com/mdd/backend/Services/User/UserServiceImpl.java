package com.mdd.backend.Services.User;

import com.mdd.backend.Exceptions.UserAlreadyExistsException;
import com.mdd.backend.Exceptions.UserNotFoundException;
import com.mdd.backend.Models.Dto.AuthDTO.UserRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserResponse;
import com.mdd.backend.Models.Dto.AuthDTO.UserWithSubscriptionsDTO;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Role;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserResponse register(UserRequest user) {

        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Email ou nom d'utilisateur déjà utilisé");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = convertDtoToEntity(user);

        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);

        return convertEntityToDto(savedUser);
    }

    @Override
    public Optional<User> findByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
        return convertEntityToDto(user);
    }

    @Override
    public UserResponse updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());

        if (StringUtils.isNotEmpty(updatedUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }


        return convertEntityToDto(userRepository.save(user));
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email utilisateur introuvable"));
    }

    @Override
    public User convertDtoToEntity(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

    @Override
    public UserResponse convertEntityToDto(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserWithSubscriptionsDTO convertToUserWithSubscriptions(User user) {
        List<SubjectResponse> subjects = user.getSubscriptions().stream()
                .map(subscription -> modelMapper.map(subscription.getSubject(), SubjectResponse.class))
                .collect(Collectors.toList());

        return UserWithSubscriptionsDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .subscriptions(subjects)
                .build();
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        user.setEmail(updatedUser.getEmail());
        user.setUsername(updatedUser.getUsername());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return convertEntityToDto(userRepository.save(user));
    }
}