package com.clinical.dto.trial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시험 검색 키워드 응답 DTO
 */
public class TrialSearchKeywordResponseDTO {

    /**
     * 목록 조회 응답 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private Long keywordGroupId;
        private String keyword;
        private LocalDateTime createdAt;
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
        private Long keywordGroupId;
        private String keyword;
        private LocalDateTime createdAt;
        
        // 추가 정보
        private String groupName;  // 속한 그룹명 (trial_keyword_group.group_name)
        private String tabName;    // 속한 탭명 (trial_keyword_group.tab_name)
    }

    /**
     * 간단 응답 DTO (키워드만)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {
        private Long id;
        private String keyword;
    }
}