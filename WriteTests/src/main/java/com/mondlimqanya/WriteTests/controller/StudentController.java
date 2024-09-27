package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.entity.*;
import com.mondlimqanya.WriteTests.repository.StudentRepository;
import com.mondlimqanya.WriteTests.repository.TestSubmissionRepository;
import com.mondlimqanya.WriteTests.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private final TestService testService; // Inject TestService

    @Autowired
    private TestSubmissionService testSubmissionService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AnswerService answerService;


    @Autowired
    public StudentController(TestService testService, StudentService studentService) {
        this.testService = testService;
        this.studentService = studentService;
    }

    // Student Login Form
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


    //Student Dashboard
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
    public String addStudent(@ModelAttribute @Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-student"; // If validation fails, return the form with error messages
        }

        // Check if the student email already exists
        if (studentService.findStudentByEmail(student.getEmailAddress()) != null) {
            model.addAttribute("error", "Email already exists");
            return "add-student";
        }

        // Set the default password and save the student
        student.setPassword("46579");
        studentService.saveStudent(student);

        return "redirect:/dashboard"; // Redirect to the dashboard or a success page
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

    @GetMapping("/all-tests")
    public String listAllTests(Model model, HttpSession session) {
        Long studentId = (Long) session.getAttribute("studentId"); // Assuming you store studentId in session
        if (studentId == null) {
            return "redirect:/student-login"; // Redirect to login if not logged in
        }

        List<Test> tests = testService.findAllTests(); // Fetch all tests
        model.addAttribute("tests", tests); // Add tests to the model
        return "student/allTests"; // Return the view name for displaying all tests
    }

    @PostMapping("/submitTest")
    public String submitTest(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        try {
            String testIdStr = params.get("testId");
            String studentIdStr = params.get("studentId");
            params.forEach((key, value) -> System.out.println(key + ": " + value));
            // Validate that testIdStr and studentIdStr are numeric before parsing
            if (testIdStr == null || !testIdStr.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid Test ID");
            }
            if (studentIdStr == null || !studentIdStr.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid Student ID");
            }

            Long testId = Long.parseLong(testIdStr);
            Long studentId = Long.parseLong(studentIdStr);

            Test test = testService.findById(testId);
            Student student = studentService.findById(studentId);

            int score = 0;
            System.out.println("Total questions: " + test.getQuestions().size());
            for (Question question : test.getQuestions()) {
                System.out.println("Processing question ID: " + question.getQuestionId());
                String submittedAnswerId = params.get("question_" + question.getQuestionId());
                if (submittedAnswerId != null && submittedAnswerId.matches("\\d+")) {
                    Long answerId = Long.parseLong(submittedAnswerId);
                    Answer submittedAnswer = answerService.findAnswerById(answerId);
                    if (submittedAnswer != null && submittedAnswer.getIsCorrect()) {
                        score += 1;  // Each correct answer increments the score by 1
                    }
                }
            }

            TestSubmission testSubmission = new TestSubmission(student, test, score);
            testSubmissionService.save(testSubmission);

            model.addAttribute("score", score);
            return "test-result";  // Redirect to a result page showing the score
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";  // Redirect to an error handling page or show an error message
        }
    }




}

