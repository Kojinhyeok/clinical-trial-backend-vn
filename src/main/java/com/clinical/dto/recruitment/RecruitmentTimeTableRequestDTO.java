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
public class RecruitmentTimeTableRequestDTO {

    private Long recruitmentId;
    private LocalDate date;
    private String timeSlot;
    private Integer maxCnt;
}