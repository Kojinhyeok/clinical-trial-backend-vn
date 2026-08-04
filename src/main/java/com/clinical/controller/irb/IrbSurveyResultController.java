package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbSurveyResultDTO;
import com.clinical.service.irb.IrbSurveyResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-results")
@RequiredArgsConstructor
public class IrbSurveyResultController {

    private final IrbSurveyResultService resultService;

    /**
     * 심사 결과 등록
     * POST /api/irb-results
     */
    @PostMapping
    public ResponseEntity<IrbSurveyResultDTO> submitResult(@RequestBody IrbSurveyResultDTO dto) {
        return ResponseEntity.ok(resultService.submitReviewResult(dto));
    }

    /**
     * 시스템 내 모든 심사 결과 조회 (관리자용)
     * GET /api/irb-results
     */
    @GetMapping
    public ResponseEntity<List<IrbSurveyResultDTO>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResult());
    }

    /**
     * 특정 IRB 과제에 대한 모든 심사 결과 조회
     * GET /api/irb-results/test/{irbTestId}
     */
    @GetMapping("/test/{irbTestId}")
    public ResponseEntity<List<IrbSurveyResultDTO>> getResultsByTestId(@PathVariable Long irbTestId) {
        return ResponseEntity.ok(resultService.getAllResultsByTestId(irbTestId));
    }

    /**
     * 특정 IRB 과제에 대한 나의 심사 결과 조회
     * GET /api/irb-results/my/{irbTestId}
     */
    @GetMapping("/my/{irbTestId}")
    public ResponseEntity<IrbSurveyResultDTO> getMyResult(
            @PathVariable Long irbTestId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(resultService.getMyResult(irbTestId, userId));
    }
    /**
     * 심사 결과 수정
     * Put /api/irb-results/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbSurveyResultDTO> update(@PathVariable Long id,@RequestBody IrbSurveyResultDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(resultService.submitReviewResult(dto));
    }
    /**
     * 심사 결과 삭제
     * Delete /api/irb-results/{id}
     */
    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id){
        resultService.delete(id);
    }
}