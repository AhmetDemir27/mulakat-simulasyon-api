package com.simulasyon.mulakat_simulasyon_api.repository;

import com.simulasyon.mulakat_simulasyon_api.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    long countByQuestion_MulakatSession_Id(long sessionId);
    List<Answer> findByQuestion_MulakatSession_Id(long sessionId);
}
