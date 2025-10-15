package com.simulasyon.mulakat_simulasyon_api.repository;

import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MulakatSessionRepository extends JpaRepository<MulakatSession,Long> {
    List<MulakatSession>findByUserId(Long userId);
}
