package com.clinical.dto.irb;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class IrbSurveyTemplateDTO {
    private Long id;
    private Long irbTestId;
    private String surveyName;
    private List<QuestionListDTO> questionList; // 문항 리스트 객체
    private LocalDateTime createdAt;
}