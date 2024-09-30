package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByLecturerEmailAddress(String emailAddress);
//    @Query("SELECT t FROM Test t JOIN t.course c JOIN c.students s WHERE s.id = :studentId")
//    List<Test> findTestsByStudentId(@Param("studentId") Long studentId);

//    @Query("SELECT t FROM Test t WHERE t.id NOT IN (SELECT ts.test.id FROM TestSubmission ts WHERE ts.student.id = :studentId AND ts.submitted = true)")
//    List<Test> findAvailableTestsForStudent(@Param("studentId") Long studentId);

//    // In TestRepository.java
//    @Query("SELECT t FROM Test t WHERE t.lecturer.lecturerId = :lecturerId")
//    List<Test> findTestsByLecturerId(@Param("lecturerId") Long lecturerId);

    @Query("SELECT t FROM Test t JOIN t.lecturer l WHERE l.lecturerId = :lecturerId AND t.id NOT IN (SELECT ts.test.id FROM TestSubmission ts WHERE ts.student.id = :studentId)")
    List<Test> findAvailableTestsByLecturerAndStudent(@Param("lecturerId") Long lecturerId, @Param("studentId") Long studentId);



}
