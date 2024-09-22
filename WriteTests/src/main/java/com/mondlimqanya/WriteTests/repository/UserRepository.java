package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAddress(String emailAddress);
}
