package com.mondlimqanya.WriteTests.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "test_submission")
public class TestSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "score")
    private int score;

    @OneToMany(mappedBy = "testSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerSubmission> answerSubmissions;

    // Constructors, Getters, and Setters

    // Default constructor if not already present
    public TestSubmission() {
    }

    // Constructor with parameters
    public TestSubmission(Student student, Test test, int score) {
        this.student = student;
        this.test = test;
        this.score = score;
    }
}

