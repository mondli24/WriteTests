package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    // Constructors
    public Course() {}

    public Course(String courseName, Lecturer lecturer) {
        this.courseName = courseName;
        this.lecturer = lecturer;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Lecturer getUser() {
        return lecturer;
    }

    public void setUser(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
