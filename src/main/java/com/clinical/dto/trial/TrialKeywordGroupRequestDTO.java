package com.clinical.dto.trial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 시험 키워드 그룹 요청 DTO
 */
public class TrialKeywordGroupRequestDTO {

    /**
     * 생성 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String tabCode;      // BASIC, MAKEUP, CLEANSER, SCALP_HAIR, ETC
        private String tabName;      // 기초 화장품, 메이크업, 세정제, 두피&헤어, 기타
        private String groupName;    // 로션,세럼,에센스
        private Integer displayOrder;
    }

    /**
     * 수정 요청 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String tabCode;
        private String tabName;
        private String groupName;
        private Integer displayOrder;
    }

    /**
     * 검색 필터 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchFilter {
        private String tabCode;      // 탭 코드 필터
        private String groupName;    // 그룹명 검색 (부분 일치)
    }
}