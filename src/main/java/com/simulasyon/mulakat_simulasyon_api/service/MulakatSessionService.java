package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import com.simulasyon.mulakat_simulasyon_api.entity.User;
import com.simulasyon.mulakat_simulasyon_api.repository.MulakatSessionRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MulakatSessionService {
    private final UserRepository userRepository;
    private final MulakatSessionRepository mulakatSessionRepository;

    public MulakatSessionService(UserRepository userRepository, MulakatSessionRepository mulakatSessionRepository) {
        this.userRepository = userRepository;
        this.mulakatSessionRepository = mulakatSessionRepository;
    }
    @Transactional
    public MulakatSession mulakatStart(Long userId, String technology){

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Bu ID'ye sahip kullanıcı bulunmuyor"));
        MulakatSession newSession = new MulakatSession();
        newSession.setUser(user);
        newSession.setTechnology(technology);

        return mulakatSessionRepository.save(newSession);
    }
}
