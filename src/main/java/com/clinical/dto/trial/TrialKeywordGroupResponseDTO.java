package com.clinical.dto.trial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시험 키워드 그룹 응답 DTO
 */
public class TrialKeywordGroupResponseDTO {

    /**
     * 목록 조회 응답 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String tabCode;
        private String tabName;
        private String groupName;
        private Integer displayOrder;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     * 상세 조회 응답 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id;
        private String tabCode;
        private String tabName;
        private String groupName;
        private Integer displayOrder;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long trialCount; // 이 탭의 시험 개수
        // keywords 제거 (trial_search_keyword 미사용)
    }

    /**
     * 탭 목록 응답 DTO (프론트 탭 렌더링용)
     * - groups 제거, 탭 자체가 최소 단위
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TabResponse {
        private Long id;
        private String tabCode;
        private String tabName;
        private Integer displayOrder;
    }

    // GroupedByTabResponse 삭제 (탭=그룹이라 불필요)
    // SuggestionResponse 삭제 (추천검색어 미사용)
}