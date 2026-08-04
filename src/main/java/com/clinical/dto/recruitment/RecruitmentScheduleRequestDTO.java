package com.clinical.dto.recruitment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentScheduleRequestDTO {
    private LocalDate scheduleDate;
    private Integer dailyMaxCnt;
    private List<TimeSlotDTO> timeSlots;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotDTO {
        private String timeSlot;
        private Integer maxCnt;
    }
}