package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.dto.AnswerDTO;
import com.mondlimqanya.WriteTests.dto.QuestionDTO;
import com.mondlimqanya.WriteTests.dto.TestDTO;
import com.mondlimqanya.WriteTests.entity.Answer;
import com.mondlimqanya.WriteTests.entity.Question;
import com.mondlimqanya.WriteTests.entity.Test;
import com.mondlimqanya.WriteTests.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Test saveTest(TestDTO testDTO) {
        Test test = new Test();
        test.setTestName(testDTO.getTestName());
        test.setDateSchedule(testDTO.getDateSchedule());
        test.setDuration(testDTO.getDuration());

        // Adding questions to the test
        for (var questionDTO : testDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuestionType(questionDTO.getQuestionType());
            question.setTest(test);

            // Adding answers to each question
            for (var answerDTO : questionDTO.getAnswers()) {
                Answer answer = new Answer();
                if (answer.getIsCorrect() == null) {
                    answer.setIsCorrect(false); // Set default value to false if not provided
                }
                answer.setAnswerText(answerDTO.getAnswerText());
                answer.setIsCorrect(answerDTO.getIsCorrect());
                answer.setQuestion(question);
                question.getAnswers().add(answer);
            }

            // Add each question to the test
            test.getQuestions().add(question);
        }

        // Save the test along with questions and answers
        return testRepository.save(test);
    }
}
