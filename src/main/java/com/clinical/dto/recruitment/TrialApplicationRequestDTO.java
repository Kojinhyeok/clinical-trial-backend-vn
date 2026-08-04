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
public class TrialApplicationRequestDTO {

    private Long recruitmentId;
    private LocalDate startDate;
    private String startTime;
    private String preferredDateTime;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private Long recruitmentFieldId;
}