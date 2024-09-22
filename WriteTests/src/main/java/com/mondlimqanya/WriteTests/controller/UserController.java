package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.entity.Student;
import com.mondlimqanya.WriteTests.entity.User;
import com.mondlimqanya.WriteTests.service.StudentService;
import com.mondlimqanya.WriteTests.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    private StudentService studentService;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    // Registration Form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Registration Handler with Validations
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Return back to registration form if validation fails
        }
        if (!user.passwordsMatch()) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        if (userService.findUserByEmail(user.getEmailAddress()) != null) {
            model.addAttribute("error", "Email is already registered");
            return "register";
        }

        // Set the user role as LECTURER (since there's only one role now)
        userService.saveUser(user);
        return "redirect:/login";
    }

    // Login Form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("emailAddress") String emailAddress,
                            @RequestParam("password") String password,
                            Model model) {
        System.out.println("Email Address: " + emailAddress);  // Debugging
        System.out.println("Password: " + password);  // Debugging

        // Fetch user from the database
        User existingUser = userService.findUserByEmail(emailAddress);
        System.out.println("Existing User: " + existingUser);  // Debugging


        // Validate user existence and password
        if (existingUser == null) {
            model.addAttribute("error", "User with this email does not exist");
            return "login";
        }

        if (!existingUser.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid password");
            return "login";
        }

        // Redirect to lecturer dashboard (since there's only one type of user now)
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        System.out.println("Dashboard endpoint hit");
        // You can add any attributes to the model here if needed
        return "dashboard";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    // GET method for displaying the form to add a student
//    @GetMapping("/add-student")
//    public String showAddStudentForm(Model model) {
//        model.addAttribute("student", new Student()); // Add an empty student object to the model
//        return "add-student";
//    }
//
//    // POST method to handle student addition
//    @PostMapping("/add-student")
//    public String addStudent(@ModelAttribute("student") @Valid Student student, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "add-student"; // Return the form if there are validation errors
//        }
//
//        // Save student with default password "46579"
//        student.setPassword("46579");
//        studentService.saveStudent(student);
//
//        // Redirect to the dashboard or success page
//        return "redirect:/dashboard";
//    }


}
