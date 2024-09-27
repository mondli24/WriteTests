package com.mondlimqanya.WriteTests.service;

import com.mondlimqanya.WriteTests.entity.Course;
import com.mondlimqanya.WriteTests.entity.Lecturer;
import com.mondlimqanya.WriteTests.dto.CourseDTO;
import com.mondlimqanya.WriteTests.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(CourseDTO courseDto, Lecturer lecturer) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setCourseDescription(courseDto.getCourseDescription());
        course.setLecturer(lecturer); // Assuming you have a setter for the lecturer

        return courseRepository.save(course);
    }

}
