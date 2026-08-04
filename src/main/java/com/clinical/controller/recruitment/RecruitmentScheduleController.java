package com.clinical.controller.recruitment;

import com.clinical.dto.recruitment.RecruitmentScheduleRequestDTO;
import com.clinical.dto.recruitment.RecruitmentScheduleResponseDTO;
import com.clinical.service.recruitment.RecruitmentScheduleService;
import com.clinical.service.recruitment.RecruitmentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitment-schedules")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentScheduleController {

    private final RecruitmentScheduleService scheduleService;
    private final RecruitmentServiceImpl recruitmentService;

    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<RecruitmentScheduleResponseDTO>> getSchedules(
            @PathVariable Long recruitmentId) {
        log.info("스케줄 조회 요청: recruitmentId={}", recruitmentId);
        List<RecruitmentScheduleResponseDTO> response =
                scheduleService.getSchedulesByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<RecruitmentScheduleResponseDTO>> saveSchedules(
            @PathVariable Long recruitmentId,
            @RequestBody List<RecruitmentScheduleRequestDTO> schedules) {
        log.info("스케줄 일괄 저장 요청: recruitmentId={}, count={}", recruitmentId, schedules.size());
        List<RecruitmentScheduleResponseDTO> response =
                scheduleService.saveSchedulesWithTimeSlots(recruitmentId, schedules);
        return ResponseEntity.ok(response);
    }
}