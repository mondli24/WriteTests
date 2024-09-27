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
}
