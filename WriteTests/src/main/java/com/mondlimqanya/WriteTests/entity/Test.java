package com.mondlimqanya.WriteTests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    private String testName;
    private Long duration;

    @Column(nullable = false)
    private LocalDate dateSchedule;


    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    public void setDuration(Duration duration) {
        this.duration = duration.toMinutes();
    }


    public Duration getDurationAsDuration() {
        return Duration.ofMinutes(duration);
    }
    public Long getLecturerId() {
        return lecturer != null ? lecturer.getLecturerId() : null;
    }

}
