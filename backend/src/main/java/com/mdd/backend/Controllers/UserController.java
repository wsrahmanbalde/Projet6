package com.mdd.backend.Controllers;

import com.mdd.backend.Models.Dto.AuthDTO.UserRequest;
import com.mdd.backend.Models.Dto.AuthDTO.UserResponse;
import com.mdd.backend.Models.User;
import com.mdd.backend.Services.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔐 Retourne le profil de l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(
                userService.convertEntityToDto(userService.getCurrentAuthenticatedUser())
        );
    }

    // 🔁 Met à jour les infos de l'utilisateur connecté
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest updatedUser) {
        Long userId = userService.getCurrentAuthenticatedUser().getId();
        return ResponseEntity.ok(userService.updateUser(userId, updatedUser));
    }
}
