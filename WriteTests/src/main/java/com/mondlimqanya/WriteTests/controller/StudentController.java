package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.entity.*;
import com.mondlimqanya.WriteTests.service.*;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private final TestService testService; // Inject TestService

    @Autowired
    private TestSubmissionService testSubmissionService;

    @Autowired
    private AnswerSubmissionService answerSubmissionService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private AnswerService answerService;


    @Autowired
    public StudentController(TestService testService, StudentService studentService) {
        this.testService = testService;
        this.studentService = studentService;
    }

     //Student Login Form
    @GetMapping("/student-login")
    public String showStudentLoginForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-login";
    }

    @PostMapping("/student-login")
    public String loginStudent(@RequestParam("emailAddress") String emailAddress,
                               @RequestParam("password") String password,
                               HttpSession session, // Include HttpSession as a parameter
                               Model model) {
        // Find student by email
        Student existingStudent = studentService.findStudentByEmail(emailAddress);
        if (existingStudent == null || !BCrypt.checkpw(password, existingStudent.getPassword())) {
            model.addAttribute("error", "Invalid email or password");
            return "student-login";
        }

        // Redirect to change password page if they logged in with default password
        if (existingStudent.getPassword().equals("46579")) {
            model.addAttribute("student", existingStudent); // Send student to the change password form
            return "redirect:/change-password"; // Adjust this to your specific change password page
        }

        // Store the student ID in the session
        session.setAttribute("studentId", existingStudent.getId());

        // Redirect to student dashboard
        return "redirect:/studentDash";
    }


//    //Student Dashboard
//    @GetMapping("/studentDash")
//    public String showStudentDashboard() {
//        return "studentDash";
//    }
    @GetMapping("/studentDash")
    public String studentDashboard(Model model, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        System.out.println("Student ID from session: " + studentId); // Debugging

        if (studentId == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        // Fetch all tests
        List<Test> tests = testService.findAllTests(); // Fetch all tests available
        model.addAttribute("tests", tests); // Add the list of tests to the model

        return "studentDash"; // Return the view name for the dashboard
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("student", new Student()); // Pass in the student object
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changeStudentPassword(@ModelAttribute Student student,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        Model model) {

        // Debug to check if email is passed correctly
        System.out.println("Email Address: " + student.getEmailAddress());

        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "change-password";
        }

        // Find the student by email in the database
        Student existingStudent = studentService.findStudentByEmail(student.getEmailAddress());
        if (existingStudent == null) {
            model.addAttribute("error", "Student not found");
            return "change-password";
        }

        // Update the student's password and save
        existingStudent.setPassword(newPassword);
        studentService.saveStudent(existingStudent);

        return "redirect:/student-login"; // Redirect to login page after password change
    }


    @GetMapping("/add-student")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@ModelAttribute @Valid Student student, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            return "add-student"; // If validation fails, return to the form with error messages
        }

        Long lecturerId = (Long) session.getAttribute("lecturerId"); // Retrieve lecturer ID from session
        if (lecturerId == null) {
            model.addAttribute("error", "No lecturer logged in");
            return "redirect:/lecturer-login"; // Redirect to login page if no lecturer is found in session
        }

        Lecturer lecturer = lecturerService.findById(lecturerId);
        if (lecturer == null) {
            model.addAttribute("error", "Lecturer not found.");
            return "add-student"; // Return to the add student form with an error message
        }

        // Check if the student email already exists
        if (studentService.findStudentByEmail(student.getEmailAddress()) != null) {
            model.addAttribute("error", "Email already exists");
            return "add-student"; // Return to the form if email exists
        }

        // Set the lecturer of the student
        student.setLecturer(lecturer);

        // Set the default password (Consider using encrypted passwords)
        student.setPassword("46579");
        studentService.saveStudent(student);

        model.addAttribute("success", "Student added successfully!");

        // Redirect to a relevant page, maybe list of students or dashboard
        return "redirect:/dashboard";
    }


    @GetMapping("/sign_out")
    public String logout(HttpSession session) {
        // Invalidate the session to log out the lecturer
        session.invalidate();
        return "student-login"; // Redirect to the login page
    }

    @GetMapping("/take-test/{testId}")
    public String takeTest(@PathVariable Long testId, Model model, HttpSession session) {
        // Fetch the test by testId
        Test test = testService.findById(testId);

        // Get the student from the session (assuming it's stored after login)
        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentService.findById(studentId);

        // Add test and student to the model
        model.addAttribute("test", test);
        model.addAttribute("student", student);

        return "takeTest"; // Thymeleaf template name
    }

//    @GetMapping("/all-tests")
//    public String listAllTests(Model model, HttpSession session) {
//        Long studentId = (Long) session.getAttribute("studentId"); // Assuming you store studentId in session
//        if (studentId == null) {
//            return "redirect:/student-login"; // Redirect to login if not logged in
//        }
//
//        List<Test> tests = testService.findAllTests(); // Fetch all tests
//        model.addAttribute("tests", tests); // Add tests to the model
//        return "student/allTests"; // Return the view name for displaying all tests
//    }

    @PostMapping("/submitTest")
    public String submitTest(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        try {
            Long testId = Long.parseLong(params.get("testId"));
            Long studentId = Long.parseLong(params.get("studentId"));

            Test test = testService.findById(testId);
            Student student = studentService.findById(studentId);

            TestSubmission testSubmission = new TestSubmission(student, test, 0);
            List<AnswerSubmission> answerSubmissions = new ArrayList<>();

            for (Question question : test.getQuestions()) {
                String submittedAnswerValue = params.get("question_" + question.getQuestionId());
                AnswerSubmission answerSubmission = new AnswerSubmission();
                answerSubmission.setTestSubmission(testSubmission);
                answerSubmission.setQuestion(question);

                if (question.getQuestionType().equals("TRUE_FALSE")) {
                    // Handle True/False question
                    boolean selectedAnswer = Boolean.parseBoolean(submittedAnswerValue);
                    answerSubmission.setSelected(selectedAnswer);

                    // Check if the selected answer matches the correct answer
                    Answer correctAnswer = question.getAnswers().stream()
                            .filter(Answer::getIsCorrect)
                            .findFirst()
                            .orElse(null);

                    if (correctAnswer != null && (selectedAnswer == correctAnswer.getIsCorrect())) {
                        testSubmission.setScore(testSubmission.getScore() + 1);
                    }
                } else {
                    // Handle Multiple Choice question
                    Long answerId = Long.parseLong(submittedAnswerValue);
                    Answer submittedAnswer = answerService.findAnswerById(answerId);
                    answerSubmission.setSubmittedAnswer(submittedAnswer);
                    answerSubmission.setSelected(submittedAnswer.getIsCorrect());

                    if (submittedAnswer.getIsCorrect()) {
                        testSubmission.setScore(testSubmission.getScore() + 1);
                    }
                }
                answerSubmissions.add(answerSubmission);
            }

            testSubmission.setAnswerSubmissions(answerSubmissions);
            testSubmissionService.save(testSubmission);

            model.addAttribute("score", testSubmission.getScore());
            model.addAttribute("totalQuestions", test.getQuestions().size());
            model.addAttribute("testId", test.getId());
            return "test-result";  // Redirect to the test result page
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid number format for test or student ID.");
            return "errorPage";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "errorPage";
        }
    }



    @GetMapping("/reviewTest/{testId}")
    public String reviewTest(@PathVariable Long testId, HttpSession session, Model model) {
        Long studentId = (Long) session.getAttribute("studentId");

        // Check if the student is logged in
        if (studentId == null) {
            return "redirect:/student-login"; // Redirect to login if no session exists
        }

        // Fetch all TestSubmissions for the given student and test
        List<TestSubmission> submissions = testSubmissionService.findAllByTestIdAndStudentId(testId, studentId);
        if (submissions.isEmpty()) {
            model.addAttribute("error", "No test submissions found for the given test and student.");
            return "errorPage";
        }

        // For example, use the most recent submission if multiple exist
        TestSubmission latestSubmission = submissions.get(submissions.size() - 1);

        // Fetch the associated AnswerSubmissions for the test submission
        List<AnswerSubmission> answerSubmissions = answerSubmissionService.findByTestSubmissionId(latestSubmission.getId());

        // Add necessary attributes to the model for the view
        model.addAttribute("submission", latestSubmission);
        model.addAttribute("test", latestSubmission.getTest());
        model.addAttribute("answerSubmissions", answerSubmissions);

        // Render the reviewTest view
        return "reviewTest";
    }

    @GetMapping("/available-tests")
    public String showAvailableTests(Model model, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        try {
            List<Test> availableTests = testService.getAvailableTestsForStudent(studentId);
            List<Long> submittedTestIds = testService.getSubmittedTestIdsByStudent(studentId);

            model.addAttribute("tests", availableTests);
            model.addAttribute("submittedTestIds", submittedTestIds);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Error retrieving tests: " + e.getMessage());
            // Optionally, you could redirect to an error page or log this error
        }

        return "available-tests"; // Name of the Thymeleaf template
    }



}

