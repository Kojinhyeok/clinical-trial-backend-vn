package com.clinical.controller.recruitment;

import com.clinical.dto.recruitment.TrialApplicationRequestDTO;
import com.clinical.dto.recruitment.TrialApplicationResponseDTO;
import com.clinical.service.recruitment.TrialApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-applications")
@RequiredArgsConstructor
@Slf4j
public class TrialApplicationController {

    private final TrialApplicationService applicationService;

    @PostMapping
    public ResponseEntity<TrialApplicationResponseDTO> createApplication(
            @RequestBody TrialApplicationRequestDTO requestDTO) {
        log.info("참여 신청 요청: {}", requestDTO);
        TrialApplicationResponseDTO response = applicationService.createApplication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialApplicationResponseDTO> updateApplication(
            @PathVariable Long id,
            @RequestBody TrialApplicationRequestDTO requestDTO) {
        log.info("신청 수정 요청: id={}, data={}", id, requestDTO);
        TrialApplicationResponseDTO response = applicationService.updateApplication(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        log.info("신청 취소 요청: id={}", id);
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TrialApplicationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("신청 상태 변경 요청: id={}, status={}", id, status);
        TrialApplicationResponseDTO response = applicationService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialApplicationResponseDTO> getApplicationById(@PathVariable Long id) {
        log.info("신청 조회 요청: id={}", id);
        TrialApplicationResponseDTO response = applicationService.getApplicationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TrialApplicationResponseDTO>> getAllApplications() {
        log.info("전체 신청 조회 요청");
        List<TrialApplicationResponseDTO> response = applicationService.getAllApplications();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<List<TrialApplicationResponseDTO>> getApplicationsByRecruitmentId(
            @PathVariable Long recruitmentId) {
        log.info("모집공고별 신청 조회 요청: recruitmentId={}", recruitmentId);
        List<TrialApplicationResponseDTO> response =
                applicationService.getApplicationsByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<TrialApplicationResponseDTO>> getApplicationsByPhone(@PathVariable String phone) {
        log.info("휴대폰 번호로 신청 조회 요청: phone={}", phone);
        List<TrialApplicationResponseDTO> response = applicationService.getApplicationsByPhone(phone);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> checkDuplicate(
            @RequestParam Long recruitmentId,
            @RequestParam String phone) {
        log.info("중복 신청 확인 요청: recruitmentId={}, phone={}", recruitmentId, phone);
        boolean isDuplicate = applicationService.isDuplicateApplication(recruitmentId, phone);
        return ResponseEntity.ok(isDuplicate);
    }
}