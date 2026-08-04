package com.clinical.controller.recruitment;

import com.clinical.dto.recruitment.RecruitmentRequestDTO;
import com.clinical.dto.recruitment.RecruitmentResponseDTO;
import com.clinical.dto.recruitment.RecruitmentSummaryDTO;
import com.clinical.service.recruitment.RecruitmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/recruitments")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseEntity<RecruitmentResponseDTO> createRecruitment(@RequestBody RecruitmentRequestDTO requestDTO) {
        log.info("모집공고 생성 요청: {}", requestDTO);
        RecruitmentResponseDTO response = recruitmentService.createRecruitment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RecruitmentResponseDTO>> createRecruitmentBulk(
            @RequestBody RecruitmentRequestDTO requestDTO) {
        log.info("모집공고 일괄 생성 요청: fieldIds={}", requestDTO.getRecruitmentFieldIds());
        List<RecruitmentResponseDTO> responses = recruitmentService.createRecruitmentBulk(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruitmentResponseDTO> updateRecruitment(
            @PathVariable Long id,
            @RequestBody RecruitmentRequestDTO requestDTO) {
        log.info("모집공고 수정 요청: id={}, data={}", id, requestDTO);
        RecruitmentResponseDTO response = recruitmentService.updateRecruitment(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitment(@PathVariable Long id) {
        log.info("모집공고 삭제 요청: id={}", id);
        recruitmentService.deleteRecruitment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentResponseDTO> getRecruitmentById(@PathVariable Long id) {
        log.info("모집공고 조회 요청: id={}", id);
        RecruitmentResponseDTO response = recruitmentService.getRecruitmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RecruitmentResponseDTO>> getAllRecruitments() {
        log.info("전체 모집공고 조회 요청");
        List<RecruitmentResponseDTO> response = recruitmentService.getAllRecruitments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecruitmentsIncludingTemp(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecruitmentSummaryDTO> result = recruitmentService.getRecruitmentsForAdmin(keyword, status, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/field/{fieldCode}")
    public ResponseEntity<List<RecruitmentSummaryDTO>> getRecruitmentsByField(@PathVariable String fieldCode) {
        log.info("지역별 모집공고 조회 요청: fieldCode={}", fieldCode);
        List<RecruitmentSummaryDTO> response = recruitmentService.getRecruitmentsByField(fieldCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<RecruitmentResponseDTO>> getRecruitmentsByDate(@PathVariable LocalDate date) {
        log.info("날짜별 모집공고 조회 요청: date={}", date);
        List<RecruitmentResponseDTO> response = recruitmentService.getRecruitmentsByDate(date);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/touch")
    public ResponseEntity<Void> touchRecruitment(@PathVariable Long id) {
        recruitmentService.touch(id);
        return ResponseEntity.ok().build();
    }
}