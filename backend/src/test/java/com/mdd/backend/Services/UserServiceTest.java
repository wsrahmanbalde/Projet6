package com.mdd.backend.Services;

import com.mdd.backend.Exceptions.UserAlreadyExistsException;
import com.mdd.backend.Models.Dto.AuthDTO.UserRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserResponse;
import com.mdd.backend.Models.Role;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.UserRepository;
import com.mdd.backend.Services.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(userRepository, modelMapper, passwordEncoder);
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(request));

        assertEquals("Email ou nom d'utilisateur déjà utilisé", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(request));

        assertEquals("Email ou nom d'utilisateur déjà utilisé", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldCreateUser_WhenEmailAndUsernameNotExist() {
        UserRequest request = new UserRequest();
        request.setEmail("test@test.com");
        request.setUsername("testuser");
        request.setPassword("password");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setEmail("test@test.com");
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("test@test.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
    }
}