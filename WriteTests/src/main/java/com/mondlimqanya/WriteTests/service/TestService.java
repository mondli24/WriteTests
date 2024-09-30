package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.dto.AnswerDTO;
import com.mondlimqanya.WriteTests.dto.QuestionDTO;
import com.mondlimqanya.WriteTests.dto.TestDTO;
import com.mondlimqanya.WriteTests.entity.*;
import com.mondlimqanya.WriteTests.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestSubmissionRepository testSubmissionRepository;

    @Autowired
    private  StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LecturerRepository lecturerRepository;


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Transactional
    public void saveTest(TestDTO testDTO, String lecturerEmail, Long courseId) {
        // Fetch the lecturer by email
        System.out.println("Lecturer email from session: " + lecturerEmail);
        Lecturer lecturer = lecturerRepository.findByEmailAddress(lecturerEmail);
        if (lecturer == null) {
            throw new RuntimeException("Lecturer not found");
        }

        // Fetch the course by courseId
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Ensure that the course is taught by the lecturer
        if (!course.getLecturer().getLecturerId().equals(lecturer.getLecturerId())) {
            throw new RuntimeException("Lecturer not authorized to create tests for this course");
        }

        // Create the test entity
        Test test = new Test();
        test.setTestName(testDTO.getTestName());
        test.setDateSchedule(testDTO.getDateSchedule());

        // Associate the lecturer and course with the test
        test.setLecturer(lecturer);  // Sets the lecturerId implicitly
        test.setCourse(course);  // Sets the courseId implicitly

        // Handle the duration field
        if (testDTO.getDuration() != null && !testDTO.getDuration().isEmpty()) {
            String[] parts = testDTO.getDuration().split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
            test.setDuration(duration);
        } else {
            test.setDuration(Duration.ZERO);
        }

        // Save the test
        testRepository.save(test);

        // Save the questions and answers
        for (QuestionDTO questionDTO : testDTO.getQuestions()) {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuestionType(questionDTO.getQuestionType());  // Set type
            question.setTest(test);  // Link the question to the test

            // Handle True/False questions
            if (questionDTO.getQuestionType().equalsIgnoreCase("true_false")) {
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

            // Save the question and answers
            questionRepository.save(question);
        }
    }

    public List<Test> findTestsByLecturerEmail(String lecturerEmail) {
        return testRepository.findByLecturerEmailAddress(lecturerEmail);
    }

//    public List<Test> findTestsByStudentId(Long studentId) {
//        // Logic to find tests based on studentId
//        return testRepository.findTestsByStudentId(studentId); // Implement this query in your repository
//    }
    public Test findById(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
    }
    public List<Test> findAllTests() {
        return testRepository.findAll();
    }
    // In TestService.java
//    public List<Test> getTestsForStudent(Long studentId) {
//        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
//        return testRepository.findTestsByLecturerId(student.getLecturer().getLecturerId());
//    }

//    public List<Test> getAvailableTestsForStudent(Long studentId) {
//        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
//        return testRepository.findTestsForStudent(student.getLecturer().getLecturerId(), studentId);
//    }

    public List<Test> getAvailableTestsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        Long lecturerId = student.getLecturer().getLecturerId();
        return testRepository.findAvailableTestsByLecturerAndStudent(lecturerId, studentId);
    }

    public List<Long> getSubmittedTestIdsByStudent(Long studentId) {
        // This would fetch all test submissions by the student and extract the test IDs
        return testSubmissionRepository.findAllByStudentId(studentId)
                .stream()
                .map(TestSubmission::getTest)
                .map(Test::getId)
                .collect(Collectors.toList());
    }


}