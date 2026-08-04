package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentSummaryDTO {

    private Long id;
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
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}