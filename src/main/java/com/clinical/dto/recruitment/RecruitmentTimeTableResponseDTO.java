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
public class RecruitmentTimeTableResponseDTO {

    private Long id;
    private Long recruitmentId;
    private LocalDate date;
    private String timeSlot;
    private Integer maxCnt;
    private Integer count;
    private LocalDateTime createdAt;
}