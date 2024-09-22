package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    Lecturer findByEmailAddress(String emailAddress);
}
