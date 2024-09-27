package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank(message = "Course name is required")
    private String courseName;

    private String courseDescription;

    // A course is taught by one lecturer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = true)
    private Lecturer lecturer;

    // A course can have many tests
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();
    // Constructors
    public Course() {}

    public Course(String courseName, Lecturer lecturer) {
        this.courseName = courseName;
        this.lecturer = lecturer;
    }

    // Custom method to add a test to a course
    public void addTest(Test test) {
        tests.add(test);
        test.setCourse(this);
    }

    // Custom method to remove a test from a course
    public void removeTest(Test test) {
        tests.remove(test);
        test.setCourse(null);
    }
}
