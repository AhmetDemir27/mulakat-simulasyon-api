package com.simulasyon.mulakat_simulasyon_api.repository;

import com.simulasyon.mulakat_simulasyon_api.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
}
