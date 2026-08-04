package com.clinical.dto.irb;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrbSurveyResultDTO {
    private Long id;
    private Long userId;            // 심사자 ID
    private Long irbTestId;         // 심사 대상 IRB ID
    private Long surveyTemplateId;  // 사용된 심사지 템플릿 ID
    private String reviewResult;    // 결과 (APPROVED, REJECTED, PENDING 등)
    private String reviewText;      // 심사 의견/코멘트
    private LocalDateTime createdAt;
}