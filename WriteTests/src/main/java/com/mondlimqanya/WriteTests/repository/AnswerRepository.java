package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // You can define custom query methods here if needed
}
