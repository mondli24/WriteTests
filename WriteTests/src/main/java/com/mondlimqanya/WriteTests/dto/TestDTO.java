package com.mondlimqanya.WriteTests.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class TestDTO {
    private String testName;

    private int duration;
    private LocalDate dateSchedule;
    private List<QuestionDTO> questions;

    public TestDTO() {}

    public TestDTO(String testName, String testType, int duration, String status) {
        this.testName = testName;
        this.duration = duration;
    }
    // Getters and Setters
}
