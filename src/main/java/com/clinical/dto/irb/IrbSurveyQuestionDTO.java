package com.clinical.dto.irb;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrbSurveyQuestionDTO {
    private Long id;
    private String questionCategory; // DOCUMENTS, ETHICS, SCIENTIFICS, AGREEMENTS
    private String questionType;     // BOOLEAN, PREFERENCE, TEXT
    private String questionText;
    private Integer questionOrder;
    private Integer isActive;            // 0: 비활성, 1: 활성
}