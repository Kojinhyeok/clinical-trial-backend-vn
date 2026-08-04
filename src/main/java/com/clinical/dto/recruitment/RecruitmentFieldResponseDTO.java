package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentFieldResponseDTO {

    private Long id;
    private String fieldCode;
    private String fieldName;
    private LocalDateTime createdAt;
}