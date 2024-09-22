package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Answer;
import com.mondlimqanya.WriteTests.entity.Question;
import com.mondlimqanya.WriteTests.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        // Ensure that answers are correctly linked to the question
        System.out.println("Saving question: " + question);
        for (Answer answer : question.getAnswers()) {
            answer.setQuestion(question);
        }
        return questionRepository.save(question);
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id " + id));
    }


    // Other service methods...
}
