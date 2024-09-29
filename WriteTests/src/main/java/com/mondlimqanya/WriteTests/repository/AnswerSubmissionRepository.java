package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerSubmissionRepository extends JpaRepository<AnswerSubmission, Long> {

    /**
     * Custom query to find all AnswerSubmissions for a specific test submission.
     * @param testSubmissionId the ID of the test submission.
     * @return a list of AnswerSubmissions associated with the given test submission.
     */
    @Query("SELECT asub FROM AnswerSubmission asub WHERE asub.testSubmission.id = :testSubmissionId")
    List<AnswerSubmission> findByTestSubmissionId(@Param("testSubmissionId") Long testSubmissionId);
}
