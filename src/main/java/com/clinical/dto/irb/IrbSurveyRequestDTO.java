package com.clinical.dto.irb;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class IrbSurveyRequestDTO {
    private IrbSurveyAnswerDTO answerDTO;
    private IrbSurveyQuestionDTO questionDTO;
    private IrbSurveyResultDTO resultDTO;

}
