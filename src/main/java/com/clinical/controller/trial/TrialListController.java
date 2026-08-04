package com.clinical.controller.trial;

import com.clinical.dto.trial.TrialListRequestDTO;
import com.clinical.dto.trial.TrialListResponseDTO;
import com.clinical.service.trial.TrialListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TrialList Controller
 * - 시험 항목 CRUD
 * - 키워드 검색
 * - 전/후 이미지 S3 Presigned URL 제공
 */
@RestController
@RequestMapping("/api/trials")
@RequiredArgsConstructor
public class TrialListController {

    private final TrialListService trialListService;

    /**
     * 시험 생성
     * POST /api/trials
     */
    @PostMapping
    public ResponseEntity<TrialListResponseDTO.DetailResponse> create(
            @RequestBody TrialListRequestDTO.Create request) {
        TrialListResponseDTO.DetailResponse response = trialListService.createTrial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 시험 수정
     * PUT /api/trials/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrialListResponseDTO.DetailResponse> update(
            @PathVariable Long id,
            @RequestBody TrialListRequestDTO.Update request) {
        TrialListResponseDTO.DetailResponse response = trialListService.updateTrial(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 시험 삭제
     * DELETE /api/trials/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trialListService.deleteTrial(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 시험 상세 조회
     * GET /api/trials/{id}
     * - 조회수 자동 증가
     * - 전/후 이미지 S3 Presigned URL 포함
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialListResponseDTO.DetailResponse> getById(@PathVariable Long id) {
        TrialListResponseDTO.DetailResponse response = trialListService.getTrialById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 시험 목록 조회 / 그룹별 조회
     * GET /api/trials
     * GET /api/trials?keywordGroupId=1
     */
    @GetMapping
    public ResponseEntity<List<TrialListResponseDTO.ListResponse>> getAll(
            @RequestParam(required = false) Long keywordGroupId) {
        List<TrialListResponseDTO.ListResponse> response;
        
        if (keywordGroupId != null) {
            response = trialListService.getTrialsByGroup(keywordGroupId);
        } else {
            response = trialListService.getAllTrials();
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 키워드 검색 (탭 내에서 필터링)
     * GET /api/trials/search?keyword=보습
     * GET /api/trials/search?keyword=보습&tabCode=BASIC
     */
    @GetMapping("/search")
    public ResponseEntity<List<TrialListResponseDTO.ListResponse>> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(required = false) String tabCode) {
        List<TrialListResponseDTO.ListResponse> response = trialListService.searchTrialsByKeyword(keyword, tabCode);
        return ResponseEntity.ok(response);
    }
}