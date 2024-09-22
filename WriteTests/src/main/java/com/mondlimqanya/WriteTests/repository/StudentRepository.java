package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
   // Student findByEmailAddress(String emailAddress);
    //Optional<Student> findByEmailAddress(String emailAddress);
    Student findByEmailAddress(String emailAddress);
}
