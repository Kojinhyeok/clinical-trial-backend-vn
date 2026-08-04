package com.clinical.service.trial;

import com.clinical.dto.trial.TrialKeywordGroupRequestDTO;
import com.clinical.dto.trial.TrialKeywordGroupResponseDTO;

import java.util.List;

/**
 * 시험 키워드 그룹 Service (탭 전용, 그룹/추천검색어 미사용)
 */
public interface TrialKeywordGroupService {

    /**
     * 탭 생성
     */
    TrialKeywordGroupResponseDTO.DetailResponse createKeywordGroup(TrialKeywordGroupRequestDTO.Create request);

    /**
     * 탭 수정
     */
    TrialKeywordGroupResponseDTO.DetailResponse updateKeywordGroup(Long id, TrialKeywordGroupRequestDTO.Update request);

    /**
     * 탭 삭제
     */
    void deleteKeywordGroup(Long id);

    /**
     * 탭 상세 조회
     */
    TrialKeywordGroupResponseDTO.DetailResponse getKeywordGroupById(Long id);

    /**
     * 전체 탭 목록 조회 (관리자용)
     */
    List<TrialKeywordGroupResponseDTO.ListResponse> getAllKeywordGroups();

    /**
     * 탭 목록 조회 (프론트 탭 렌더링용)
     * - display_order 정렬
     */
    List<TrialKeywordGroupResponseDTO.TabResponse> getTabs();

    // 삭제된 메서드:
    // getKeywordGroupsByTab() → getTabs()로 통합
    // getSuggestedGroupNames() → 추천검색어 미사용
    // getGroupedByTab() → 탭=그룹이라 불필요
}