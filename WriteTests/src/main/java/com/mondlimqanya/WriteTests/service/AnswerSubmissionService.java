package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.AnswerSubmission;
import com.mondlimqanya.WriteTests.repository.AnswerSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerSubmissionService {

    @Autowired
    private AnswerSubmissionRepository answerSubmissionRepository;

    /**
     * Fetches all AnswerSubmissions associated with the given test submission ID.
     * @param testSubmissionId the ID of the test submission.
     * @return a list of AnswerSubmissions associated with the test submission.
     */
    public List<AnswerSubmission> findByTestSubmissionId(Long testSubmissionId) {
        return answerSubmissionRepository.findByTestSubmissionId(testSubmissionId);
    }

    /**
     * Save an AnswerSubmission.
     * @param answerSubmission the AnswerSubmission to save.
     * @return the saved AnswerSubmission.
     */
    public AnswerSubmission save(AnswerSubmission answerSubmission) {
        return answerSubmissionRepository.save(answerSubmission);
    }

    /**
     * Save a list of AnswerSubmissions.
     * @param answerSubmissions the list of AnswerSubmissions to save.
     * @return the saved list of AnswerSubmissions.
     */
    public List<AnswerSubmission> saveAll(List<AnswerSubmission> answerSubmissions) {
        return answerSubmissionRepository.saveAll(answerSubmissions);
    }
}
