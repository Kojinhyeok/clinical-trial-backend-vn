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
public class TrialApplicationResponseDTO {

    private Long id;
    private Long recruitmentId;
    private Long recruitmentFieldId;
    private String status;
    private LocalDate startDate;
    private String startTime;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}