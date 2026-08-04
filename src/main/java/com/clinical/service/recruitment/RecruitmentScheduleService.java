package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.RecruitmentScheduleRequestDTO;
import com.clinical.dto.recruitment.RecruitmentScheduleResponseDTO;

import java.util.List;

public interface RecruitmentScheduleService {

    List<RecruitmentScheduleResponseDTO> saveSchedulesWithTimeSlots(Long recruitmentId, List<RecruitmentScheduleRequestDTO> schedules);

    List<RecruitmentScheduleResponseDTO> getSchedulesByRecruitmentId(Long recruitmentId);
}