package com.clinical.controller.trial;

import com.clinical.dto.trial.TrialSearchKeywordRequestDTO;
import com.clinical.dto.trial.TrialSearchKeywordResponseDTO;
import com.clinical.service.trial.TrialSearchKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시험 검색 키워드 Controller
 */
@RestController
@RequestMapping("/api/trial-search-keywords")
@RequiredArgsConstructor
public class TrialSearchKeywordController {

    private final TrialSearchKeywordService searchKeywordService;

    /**
     * 키워드 생성 (관리자)
     * POST /api/trial-search-keywords
     */
    @PostMapping
    public ResponseEntity<TrialSearchKeywordResponseDTO.DetailResponse> createKeyword(
            @RequestBody TrialSearchKeywordRequestDTO.Create request) {
        TrialSearchKeywordResponseDTO.DetailResponse response = searchKeywordService.createKeyword(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 키워드 일괄 생성 (관리자)
     * POST /api/trial-search-keywords/bulk
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<TrialSearchKeywordResponseDTO.SimpleResponse>> bulkCreateKeywords(
            @RequestBody TrialSearchKeywordRequestDTO.BulkCreate request) {
        List<TrialSearchKeywordResponseDTO.SimpleResponse> response = searchKeywordService.bulkCreateKeywords(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 키워드 수정 (관리자)
     * PUT /api/trial-search-keywords/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrialSearchKeywordResponseDTO.DetailResponse> updateKeyword(
            @PathVariable Long id,
            @RequestBody TrialSearchKeywordRequestDTO.Update request) {
        TrialSearchKeywordResponseDTO.DetailResponse response = searchKeywordService.updateKeyword(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 키워드 삭제 (관리자)
     * DELETE /api/trial-search-keywords/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long id) {
        searchKeywordService.deleteKeyword(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 키워드 상세 조회
     * GET /api/trial-search-keywords/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialSearchKeywordResponseDTO.DetailResponse> getKeyword(@PathVariable Long id) {
        TrialSearchKeywordResponseDTO.DetailResponse response = searchKeywordService.getKeywordById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 키워드 조회
     * GET /api/trial-search-keywords
     * GET /api/trial-search-keywords?keywordGroupId=1
     */
    @GetMapping
    public ResponseEntity<List<TrialSearchKeywordResponseDTO.ListResponse>> getAllKeywords(
            @RequestParam(required = false) Long keywordGroupId) {
        List<TrialSearchKeywordResponseDTO.ListResponse> response;

        if (keywordGroupId != null) {
            response = searchKeywordService.getKeywordsByGroup(keywordGroupId);
        } else {
            response = searchKeywordService.getAllKeywords();
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 키워드로 그룹 ID 검색
     * GET /api/trial-search-keywords/search-groups?keyword=보습
     */
    @GetMapping("/search-groups")
    public ResponseEntity<List<Long>> findGroupIdsByKeyword(@RequestParam String keyword) {
        List<Long> groupIds = searchKeywordService.findGroupIdsByKeyword(keyword);
        return ResponseEntity.ok(groupIds);
    }
}