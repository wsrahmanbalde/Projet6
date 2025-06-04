package com.mdd.backend.Controllers;

import com.mdd.backend.Models.Auth.RegisterRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserResponse;
import com.mdd.backend.Models.Role;
import com.mdd.backend.Models.User;
import com.mdd.backend.Models.Auth.LoginRequest;
import com.mdd.backend.Models.Auth.AuthResponse;
import com.mdd.backend.Services.JwtService;
import com.mdd.backend.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        // Mapper manuellement vers UserRequest DTO
        UserRequest userRequest = UserRequest.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();

        // Enregistre le nouvel utilisateur
        UserResponse savedUser = userService.register(userRequest);

        // Recharge les détails de sécurité avec l'email pour utiliser un UserDetails complet
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getEmail(), // ou username si tu veux
                        request.password()      // mot de passe original, car le hashé est inutile ici
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
