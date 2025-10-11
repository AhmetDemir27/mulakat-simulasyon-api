package com.simulasyon.mulakat_simulasyon_api.controller;


import com.simulasyon.mulakat_simulasyon_api.dto.GoogleLoginRequest;
import com.simulasyon.mulakat_simulasyon_api.entity.User;
import com.simulasyon.mulakat_simulasyon_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/google/callback/")
    public ResponseEntity<User> googleLogin (@RequestBody GoogleLoginRequest request) {
        Optional<User> currentUserOpt = userService.findByUser(request.getGoogleId());

        if (currentUserOpt.isPresent()) {
            return ResponseEntity.ok(currentUserOpt.get());
        } else {
            User newUser = new User();
            newUser.setGoogleId(request.getGoogleId());
            newUser.setEmail(request.getEmail());
            newUser.setNameSurname(request.getNameSurname());
            newUser.setProfileFotoUrl(request.getProfileFotoUrl());

            User createdUser = userService.createUser(newUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
    }
}
