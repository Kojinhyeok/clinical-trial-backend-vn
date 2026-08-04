package com.clinical.dto.irb;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrbSurveyAnswerDTO {
    private Long id;
    private Long userId;
    private Long irbTestId;
    private Long surveyTemplateId;
    private Map<String, Object> answerList; // JSON String: {"q1": true, "q2": "텍스트"}
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}