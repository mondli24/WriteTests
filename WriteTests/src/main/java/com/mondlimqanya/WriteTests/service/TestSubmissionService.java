package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.TestSubmission;
import com.mondlimqanya.WriteTests.repository.TestSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestSubmissionService {

    @Autowired
    private TestSubmissionRepository testSubmissionRepository;

    /**
     * Finds all TestSubmission records by testId and studentId.
     *
     * @param testId the ID of the test
     * @param studentId the ID of the student
     * @return a list of TestSubmission objects
     */
    public List<TestSubmission> findAllByTestIdAndStudentId(Long testId, Long studentId) {
        return testSubmissionRepository.findAllByTestIdAndStudentId(testId, studentId);
    }

    /**
     * Save a TestSubmission in the repository.
     *
     * @param testSubmission the TestSubmission object to save
     * @return the saved TestSubmission object
     */
    public TestSubmission save(TestSubmission testSubmission) {
        return testSubmissionRepository.save(testSubmission);
    }
}
