package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentScheduleResponseDTO {
    private Long id;
    private Long recruitmentId;
    private LocalDate scheduleDate;
    private Integer dailyMaxCnt;
    private Integer dailyCount;
    private LocalDateTime createdAt;
    private List<RecruitmentTimeTableResponseDTO> timeSlots;
}