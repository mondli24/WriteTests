package com.mondlimqanya.WriteTests.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnswerDTO {
    private String answerText;
    private Boolean isCorrect;

    public AnswerDTO() {}

    public AnswerDTO(String answerText, boolean isCorrect) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

}
