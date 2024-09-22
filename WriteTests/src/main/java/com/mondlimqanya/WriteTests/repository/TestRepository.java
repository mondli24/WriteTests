package com.mondlimqanya.WriteTests.repository;

import com.mondlimqanya.WriteTests.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    // You can define custom query methods here if needed
}
