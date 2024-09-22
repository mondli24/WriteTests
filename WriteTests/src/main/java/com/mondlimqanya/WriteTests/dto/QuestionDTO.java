package com.mondlimqanya.WriteTests.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class QuestionDTO {
    // Getters and Setters
    private String questionText;
    private String questionType;
    private List<AnswerDTO> answers = new ArrayList<>();  // Initialize answers with an empty list
    public QuestionDTO() {}

    public QuestionDTO(String questionText, String questionType) {
        this.questionText = questionText;
        this.questionType = questionType;
    }

}
