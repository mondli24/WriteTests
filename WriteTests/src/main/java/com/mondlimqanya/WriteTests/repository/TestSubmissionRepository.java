package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.TestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubmissionRepository extends JpaRepository<TestSubmission, Long> {

    /**
     * Finds all TestSubmission records by testId and studentId.
     *
     * @param testId the ID of the test
     * @param studentId the ID of the student
     * @return a list of TestSubmission objects
     */
    @Query("SELECT ts FROM TestSubmission ts WHERE ts.test.id = :testId AND ts.student.id = :studentId")
    List<TestSubmission> findAllByTestIdAndStudentId(@Param("testId") Long testId, @Param("studentId") Long studentId);
}
