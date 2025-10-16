package com.simulasyon.mulakat_simulasyon_api.repository;

import com.simulasyon.mulakat_simulasyon_api.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByMulakatSession_Id(Long sessionId);
}
