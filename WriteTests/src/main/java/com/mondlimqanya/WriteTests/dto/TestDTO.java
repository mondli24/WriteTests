package com.mondlimqanya.WriteTests.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class TestDTO {
    private String testName;

    private String duration;
    private LocalDate dateSchedule;
    private List<QuestionDTO> questions;

    public TestDTO() {}

    // Getters and Setters
}
