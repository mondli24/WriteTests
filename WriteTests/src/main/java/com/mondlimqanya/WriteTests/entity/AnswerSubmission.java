package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answer_submission")
public class AnswerSubmission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "test_submission_id", nullable = false)
    private TestSubmission testSubmission;


    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = true)
    private Answer submittedAnswer;

    private boolean selected; // true if the student selected this answer

    // Constructors, getters, and setters
    public AnswerSubmission() {
    }

    public AnswerSubmission(TestSubmission testSubmission, Answer submittedAnswer, boolean selected) {
        this.testSubmission = testSubmission;
        this.submittedAnswer = submittedAnswer;
        this.selected = selected;
    }
}
