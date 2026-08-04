package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.RecruitmentScheduleRequestDTO;
import com.clinical.dto.recruitment.RecruitmentScheduleResponseDTO;
import com.clinical.entity.recruitment.RecruitmentSchedule;
import com.clinical.repository.recruitment.RecruitmentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecruitmentScheduleServiceImpl implements RecruitmentScheduleService {

    private final RecruitmentScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public List<RecruitmentScheduleResponseDTO> saveSchedulesWithTimeSlots(Long recruitmentId, List<RecruitmentScheduleRequestDTO> schedules) {
        scheduleRepository.deleteByRecruitmentId(recruitmentId);

        List<RecruitmentScheduleResponseDTO> result = new ArrayList<>();

        for (RecruitmentScheduleRequestDTO dto : schedules) {
            RecruitmentSchedule schedule = RecruitmentSchedule.builder()
                    .recruitmentId(recruitmentId)
                    .scheduleDate(dto.getScheduleDate())
                    .dailyMaxCnt(0)
                    .dailyCount(0)
                    .build();
            RecruitmentSchedule savedSchedule = scheduleRepository.save(schedule);

            result.add(RecruitmentScheduleResponseDTO.builder()
                    .id(savedSchedule.getId())
                    .recruitmentId(savedSchedule.getRecruitmentId())
                    .scheduleDate(savedSchedule.getScheduleDate())
                    .createdAt(savedSchedule.getCreatedAt())
                    .build());
        }

        return result;
    }

    @Override
    public List<RecruitmentScheduleResponseDTO> getSchedulesByRecruitmentId(Long recruitmentId) {
        return scheduleRepository
                .findByRecruitmentIdOrderByScheduleDateAsc(recruitmentId)
                .stream()
                .map(schedule -> RecruitmentScheduleResponseDTO.builder()
                        .id(schedule.getId())
                        .recruitmentId(schedule.getRecruitmentId())
                        .scheduleDate(schedule.getScheduleDate())
                        .createdAt(schedule.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}