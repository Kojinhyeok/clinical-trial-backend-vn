package com.clinical.dto.trial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 시험 검색 키워드 요청 DTO
 */
public class TrialSearchKeywordRequestDTO {

    /**
     * 생성 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private Long keywordGroupId;
        private String keyword;
    }

    /**
     * 일괄 생성 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkCreate {
        private Long keywordGroupId;
        private List<String> keywords;  // ["로션", "세럼", "에센스", ...]
    }

    /**
     * 수정 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String keyword;
    }

    /**
     * 검색 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;  // 검색할 키워드 (부분 일치)
    }
}