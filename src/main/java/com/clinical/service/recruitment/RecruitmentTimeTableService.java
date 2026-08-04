package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.RecruitmentTimeTableRequestDTO;
import com.clinical.dto.recruitment.RecruitmentTimeTableResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface RecruitmentTimeTableService {

    RecruitmentTimeTableResponseDTO createTimeTable(RecruitmentTimeTableRequestDTO requestDTO);

    RecruitmentTimeTableResponseDTO updateTimeTable(Long id, RecruitmentTimeTableRequestDTO requestDTO);

    void deleteTimeTable(Long id);

    List<RecruitmentTimeTableResponseDTO> getTimeTablesByRecruitmentId(Long recruitmentId);

    List<RecruitmentTimeTableResponseDTO> getTimeTablesByRecruitmentIdAndDate(Long recruitmentId, LocalDate date);

    void incrementCount(Long recruitmentId, String timeSlot);

    void decrementCount(Long recruitmentId, String timeSlot);

    void incrementCount(Long recruitmentId, LocalDate date, String timeSlot);

    void decrementCount(Long recruitmentId, LocalDate date, String timeSlot);
}