package com.clinical.controller.trial;

import com.clinical.dto.trial.TrialKeywordGroupRequestDTO;
import com.clinical.dto.trial.TrialKeywordGroupResponseDTO;
import com.clinical.service.trial.TrialKeywordGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시험 키워드 그룹 Controller (탭 전용, 그룹/추천검색어 미사용)
 */
@RestController
@RequestMapping("/api/trial-keyword-groups")
@RequiredArgsConstructor
public class TrialKeywordGroupController {

    private final TrialKeywordGroupService keywordGroupService;

    /**
     * 탭 생성 (관리자)
     * POST /api/trial-keyword-groups
     */
    @PostMapping
    public ResponseEntity<TrialKeywordGroupResponseDTO.DetailResponse> createKeywordGroup(
            @RequestBody TrialKeywordGroupRequestDTO.Create request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(keywordGroupService.createKeywordGroup(request));
    }

    /**
     * 탭 수정 (관리자)
     * PUT /api/trial-keyword-groups/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrialKeywordGroupResponseDTO.DetailResponse> updateKeywordGroup(
            @PathVariable Long id,
            @RequestBody TrialKeywordGroupRequestDTO.Update request) {
        return ResponseEntity.ok(keywordGroupService.updateKeywordGroup(id, request));
    }

    /**
     * 탭 삭제 (관리자)
     * DELETE /api/trial-keyword-groups/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeywordGroup(@PathVariable Long id) {
        keywordGroupService.deleteKeywordGroup(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 탭 상세 조회
     * GET /api/trial-keyword-groups/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialKeywordGroupResponseDTO.DetailResponse> getKeywordGroup(
            @PathVariable Long id) {
        return ResponseEntity.ok(keywordGroupService.getKeywordGroupById(id));
    }

    /**
     * 전체 탭 목록 조회 (관리자용)
     * GET /api/trial-keyword-groups
     */
    @GetMapping
    public ResponseEntity<List<TrialKeywordGroupResponseDTO.ListResponse>> getAllKeywordGroups() {
        return ResponseEntity.ok(keywordGroupService.getAllKeywordGroups());
    }

    /**
     * 탭 목록 조회 (프론트 탭 렌더링용)
     * GET /api/trial-keyword-groups/tabs
     */
    @GetMapping("/tabs")
    public ResponseEntity<List<TrialKeywordGroupResponseDTO.TabResponse>> getTabs() {
        return ResponseEntity.ok(keywordGroupService.getTabs());
    }

    // 삭제된 엔드포인트:
    // GET /api/trial-keyword-groups/suggestions → 추천검색어 미사용
    // GET /api/trial-keyword-groups/grouped     → /tabs 로 대체
}