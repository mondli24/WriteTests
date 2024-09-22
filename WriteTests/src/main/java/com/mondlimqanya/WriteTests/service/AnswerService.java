package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Answer;
import com.mondlimqanya.WriteTests.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Answer findAnswerById(Long id) {
        return answerRepository.findById(id).orElse(null);
    }

    // Other service methods...
}
