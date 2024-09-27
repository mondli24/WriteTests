package com.mondlimqanya.WriteTests.controller;

import com.mondlimqanya.WriteTests.dto.CourseDTO;
import com.mondlimqanya.WriteTests.entity.Course;
import com.mondlimqanya.WriteTests.entity.Lecturer;
import com.mondlimqanya.WriteTests.service.CourseService;
import com.mondlimqanya.WriteTests.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private LecturerService lecturerService;



    // Show the form to add a new course
    @GetMapping("/add-course")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        return "add-course";
    }

    // Handle the form submission to create a new course
    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute CourseDTO courseDto, HttpSession session, Model model) {
        Long lecturerId = (Long) session.getAttribute("lecturerId"); // Retrieve lecturer ID from session
        if (lecturerId == null) {
            model.addAttribute("error", "Lecturer ID not found in session.");
            return "redirect:/login"; // Return to the add course form with an error message
        }

        Lecturer lecturer = lecturerService.findById(lecturerId);
        if (lecturer == null) {
            model.addAttribute("error", "Lecturer not found.");
            return "add-course"; // Return to the add course form with an error message
        }

        // Create the course
        Course course = courseService.createCourse(courseDto, lecturer); // Call the createCourse method

        // Optionally, you can add a success message
        model.addAttribute("success", "Course created successfully!");

        // Redirect to the dashboard page
        return "redirect:/tests/create?courseId=" + course.getCourseId(); // Redirect to the dashboard page
    }



}
