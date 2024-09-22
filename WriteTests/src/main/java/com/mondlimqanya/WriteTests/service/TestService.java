package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.dto.AnswerDTO;
import com.mondlimqanya.WriteTests.dto.QuestionDTO;
import com.mondlimqanya.WriteTests.dto.TestDTO;
import com.mondlimqanya.WriteTests.entity.Answer;
import com.mondlimqanya.WriteTests.entity.Lecturer;
import com.mondlimqanya.WriteTests.entity.Question;
import com.mondlimqanya.WriteTests.entity.Test;
import com.mondlimqanya.WriteTests.repository.AnswerRepository;
import com.mondlimqanya.WriteTests.repository.LecturerRepository;
import com.mondlimqanya.WriteTests.repository.QuestionRepository;
import com.mondlimqanya.WriteTests.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public void saveTest(TestDTO testDTO,String lecturerEmail) {
        // Fetch the lecturer by email
        System.out.println("Lecturer email from session: " + lecturerEmail);
        Lecturer lecturer = lecturerRepository.findByEmailAddress(lecturerEmail);


        if (lecturer == null) {
            throw new RuntimeException("Lecturer not found");
        }
        Test test = new Test();
        test.setTestName(testDTO.getTestName());
        test.setDateSchedule(testDTO.getDateSchedule());
        test.setLecturer(lecturer);

        if (testDTO.getDuration() != null && !testDTO.getDuration().isEmpty()) {
            String[] parts = testDTO.getDuration().split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
            test.setDuration(duration);
        } else {
            test.setDuration(Duration.ZERO);
        }


        testRepository.save(test);

        for (QuestionDTO questionDTO : testDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuestionType(questionDTO.getQuestionType());  // Set type

            // Set the test for the question
            question.setTest(test);  // Link the question to the test

            // Handle True/False questions
            if (questionDTO.getQuestionType().equalsIgnoreCase("true_false")) {
                // Create two answer options: True and False
                Answer trueAnswer = new Answer("True", questionDTO.getCorrectAnswer());
                Answer falseAnswer = new Answer("False", !questionDTO.getCorrectAnswer());

                question.getAnswers().add(trueAnswer);
                question.getAnswers().add(falseAnswer);

                trueAnswer.setQuestion(question);
                falseAnswer.setQuestion(question);
            }
            // Handle Multiple Choice questions
            else if (questionDTO.getQuestionType().equalsIgnoreCase("multiple_choice")) {
                for (AnswerDTO answerDTO : questionDTO.getAnswers()) {
                    Answer answer = new Answer();
                    answer.setAnswerText(answerDTO.getAnswerText());
                    answer.setIsCorrect(answerDTO.getIsCorrect());  // Is this answer correct?
                    question.getAnswers().add(answer);
                    answer.setQuestion(question);  // Link the answer to the question
                }
            }

            // Save the question and answers in the database
            questionRepository.save(question);
        }
    }
}