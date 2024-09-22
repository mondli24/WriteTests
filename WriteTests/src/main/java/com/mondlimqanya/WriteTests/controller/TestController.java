package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.dto.TestDTO;
import com.mondlimqanya.WriteTests.entity.Test;
import com.mondlimqanya.WriteTests.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    // Handles GET request to display the test creation form
    @GetMapping("/create")
    public String showCreateTestForm(Model model) {
        model.addAttribute("testDTO", new TestDTO());  // Add an empty TestDTO object to the model for form binding
        return "create-test";  // Return the name of the Thymeleaf template
    }

    // Handles POST request to submit the form and create a new test
    @PostMapping("/create")
    public String createTest(@ModelAttribute TestDTO testDTO, Model model) {
        testService.saveTest(testDTO);
        model.addAttribute("message", "Test created successfully");
        return "redirect:/tests/create";
    }
}
