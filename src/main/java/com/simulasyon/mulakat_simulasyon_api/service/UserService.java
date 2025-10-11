package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.entity.User;
import com.simulasyon.mulakat_simulasyon_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUser(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public User createUser(User newUser){
        return userRepository.save(newUser);
    }
}
