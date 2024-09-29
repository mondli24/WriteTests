package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.entity.Lecturer;
import com.mondlimqanya.WriteTests.entity.Test;
import com.mondlimqanya.WriteTests.service.StudentService;
import com.mondlimqanya.WriteTests.service.LecturerService;
import com.mondlimqanya.WriteTests.service.TestService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class LecturerController {

    private static final Logger logger = LoggerFactory.getLogger(LecturerController.class);

    @Autowired
    private LecturerService lecturerService;
    private StudentService studentService;

    @Autowired
    private TestService testService;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/index";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("lecturer", new Lecturer());
        return "register";
    }


    @PostMapping("/register")
    public String registerLecturer(@ModelAttribute @Valid Lecturer lecturer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Return back to registration form if validation fails
        }
        if (!lecturer.passwordsMatch()) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        if (lecturerService.findLecturerByEmail(lecturer.getEmailAddress()) != null) {
            model.addAttribute("error", "Email is already registered");
            return "register";
        }

        // Set the lecturer role as LECTURER (since there's only one role now)
        lecturerService.saveLecturer(lecturer);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("lecturer", new Lecturer());
        return "login";
    }

    @GetMapping("/index")
    public String showIndexForm() {
        return "index";  // Returns the index.html template
    }


    @PostMapping("/login")
    public String loginLecturer(@RequestParam("emailAddress") String emailAddress,
                                @RequestParam("password") String password,
                                Model model,
                                HttpSession session) {
        System.out.println("Email Address: " + emailAddress);  // Debugging

        Lecturer existingLecturer = lecturerService.findLecturerByEmail(emailAddress);
        System.out.println("Existing lecturer: " + existingLecturer);  // Debugging

        if (existingLecturer == null) {
            model.addAttribute("error", "Lecturer with this email does not exist");
            return "login";
        }

        // Use the authenticateLecturer method to verify the password
        if (!lecturerService.authenticateLecturer(emailAddress, password)) {
            model.addAttribute("error", "Invalid password");
            return "login";
        }

        // Store the lecturer's ID and email in the session
        session.setAttribute("lecturerId", existingLecturer.getLecturerId());
        session.setAttribute("lecturerEmail", emailAddress);  // Store lecturerEmail as well

        return "redirect:/dashboard";
    }



    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        System.out.println("Dashboard endpoint hit");
        return "dashboard";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log out the lecturer
        session.invalidate();
        return "login"; // Redirect to the login page
    }

    @GetMapping("/my-tests")
    public String viewMyTests(Model model, HttpSession session) {
        String lecturerEmail = (String) session.getAttribute("lecturerEmail");
        if (lecturerEmail == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        List<Test> tests = testService.findTestsByLecturerEmail(lecturerEmail);
        model.addAttribute("tests", tests);

        return "my-tests"; // The name of your Thymeleaf template
    }

}
