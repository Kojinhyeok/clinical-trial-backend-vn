package com.clinical.controller.recruitment;

import com.clinical.dto.recruitment.RecruitmentTimeTableRequestDTO;
import com.clinical.dto.recruitment.RecruitmentTimeTableResponseDTO;
import com.clinical.service.recruitment.RecruitmentTimeTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/recruitment-time-tables")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentTimeTableController {

    private final RecruitmentTimeTableService timeTableService;

    @PostMapping
    public ResponseEntity<RecruitmentTimeTableResponseDTO> createTimeTable(
            @RequestBody RecruitmentTimeTableRequestDTO requestDTO) {
        log.info("시간대 생성 요청: {}", requestDTO);
        RecruitmentTimeTableResponseDTO response = timeTableService.createTimeTable(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentTimeTableResponseDTO> updateTimeTable(
            @PathVariable Long id,
            @RequestBody RecruitmentTimeTableRequestDTO requestDTO) {
        log.info("시간대 수정 요청: id={}, data={}", id, requestDTO);
        RecruitmentTimeTableResponseDTO response = timeTableService.updateTimeTable(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id) {
        log.info("시간대 삭제 요청: id={}", id);
        timeTableService.deleteTimeTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<RecruitmentTimeTableResponseDTO>> getTimeTablesByRecruitmentId(
            @PathVariable Long recruitmentId) {
        log.info("모집공고별 시간대 조회 요청: recruitmentId={}", recruitmentId);
        List<RecruitmentTimeTableResponseDTO> response =
                timeTableService.getTimeTablesByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruitment/{recruitmentId}/date/{date}")
    public ResponseEntity<List<RecruitmentTimeTableResponseDTO>> getTimeTablesByDate(
            @PathVariable Long recruitmentId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("날짜별 시간대 조회 요청: recruitmentId={}, date={}", recruitmentId, date);
        List<RecruitmentTimeTableResponseDTO> response =
                timeTableService.getTimeTablesByRecruitmentIdAndDate(recruitmentId, date);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/increment")
    public ResponseEntity<Void> incrementCount(
            @RequestParam Long recruitmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String timeSlot) {
        log.info("시간대 인원 증가 요청: recruitmentId={}, date={}, timeSlot={}", recruitmentId, date, timeSlot);
        if (date != null) {
            timeTableService.incrementCount(recruitmentId, date, timeSlot);
        } else {
            timeTableService.incrementCount(recruitmentId, timeSlot);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrement")
    public ResponseEntity<Void> decrementCount(
            @RequestParam Long recruitmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String timeSlot) {
        log.info("시간대 인원 감소 요청: recruitmentId={}, date={}, timeSlot={}", recruitmentId, date, timeSlot);
        if (date != null) {
            timeTableService.decrementCount(recruitmentId, date, timeSlot);
        } else {
            timeTableService.decrementCount(recruitmentId, timeSlot);
        }
        return ResponseEntity.ok().build();
    }
}