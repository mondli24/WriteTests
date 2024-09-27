package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.TestSubmission;
import com.mondlimqanya.WriteTests.repository.TestSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestSubmissionService {
    @Autowired
    private TestSubmissionRepository repository;

    public TestSubmission save(TestSubmission testSubmission) {
        return repository.save(testSubmission);
    }
}

