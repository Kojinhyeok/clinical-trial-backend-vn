package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.clinical.dto.file.FileResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentResponseDTO {

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
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<FileResponseDTO> attachedFiles;
}