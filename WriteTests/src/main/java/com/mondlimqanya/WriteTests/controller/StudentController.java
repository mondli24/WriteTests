package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.entity.Student;
import com.mondlimqanya.WriteTests.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Student Login Form
    @GetMapping("/student-login")
    public String showStudentLoginForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-login";
    }

    @PostMapping("/student-login")
    public String loginStudent(@RequestParam("emailAddress") String emailAddress,
                               @RequestParam("password") String password, Model model) {
        // Find student by email
        Student existingStudent = studentService.findStudentByEmail(emailAddress);
        if (existingStudent == null) {
            model.addAttribute("error", "Student with this email does not exist");
            return "student-login";
        }

        if (!existingStudent.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid password");
            return "student-login";
        }

        // Redirect to change password page if they logged in with default password
        if (existingStudent.getPassword().equals("46579")) {
            model.addAttribute("student", existingStudent); // Send student to the change password form
            return "redirect:/studentDash";
        }

        // Redirect to student dashboard
        return "redirect:/studentDash";
    }

    // Student Dashboard
    @GetMapping("/studentDash")
    public String showStudentDashboard() {
        return "studentDash";
    }

    // Change Password Form
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

}
