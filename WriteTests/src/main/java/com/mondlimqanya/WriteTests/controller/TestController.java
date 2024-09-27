package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.dto.TestDTO;
import com.mondlimqanya.WriteTests.entity.Test;
import com.mondlimqanya.WriteTests.service.TestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    // Handles GET request to display the test creation form
    @GetMapping("/create")
    public String showCreateTestForm(@RequestParam Long courseId, Model model) {
        model.addAttribute("testDTO", new TestDTO());  // Add an empty TestDTO object to the model for form binding
        model.addAttribute("courseId", courseId);
        return "create-test";  // Return the name of the Thymeleaf template
    }

    // Handles POST request to submit the form and create a new test
    @PostMapping("/create")
    public String createTest(@ModelAttribute TestDTO testDTO, @RequestParam Long courseId, HttpSession session, Model model) {
        String lecturerEmail = (String) session.getAttribute("lecturerEmail");

        if (lecturerEmail == null) {
            model.addAttribute("error", "User not logged in");
            return "redirect:/login"; // Redirect to login page
        }

        // Save the test with the provided lecturerEmail and courseId
        testService.saveTest(testDTO, lecturerEmail, courseId);
        model.addAttribute("message", "Test created successfully");

        // Redirect to the same test creation page for the given courseId
        return "redirect:/tests/create?courseId=" + courseId;
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log out the lecturer
        session.invalidate();
        return "redirect:/login"; // Redirect to the login page
    }


}
