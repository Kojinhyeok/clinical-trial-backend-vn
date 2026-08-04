package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRequestDTO {

    private Long userId;
    private Boolean isNotification;
    private Boolean isTemp;
    private String recruitmentFieldIds;
    private String trialCode;
    private String trialName;
    private Integer participationNumber;
    private String participationGroup;
    private String trialPart;
    private String participationCost;
    private String requirements;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String content;
    private String status;
}
