package com.simulasyon.mulakat_simulasyon_api.repository;

import com.simulasyon.mulakat_simulasyon_api.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

}
