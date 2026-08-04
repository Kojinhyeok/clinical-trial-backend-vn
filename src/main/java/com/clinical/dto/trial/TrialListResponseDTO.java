package com.clinical.dto.trial;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TrialList Response DTO
 * ⚠️ 유효성 평가 페이지는 조회수 기능 없음
 */
public class TrialListResponseDTO {

    /**
     * 이미지 쌍 DTO (다중 BEFORE/AFTER 지원)
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImagePair {
        private int pairIndex;
        private String beforeImageUrl;
        private String afterImageUrl;
    }

    /**
     * 목록 조회용 Response DTO (카드 표시용)
     * - 탭별로 그룹화되어 표시
     * - 검색어로 필터링
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private Long keywordGroupId;
        private String tabCode;                    // trial_keyword_group.tab_code (조인)
        private String groupName;                  // trial_keyword_group.group_name (조인)
        private String tabName;                    // trial_keyword_group.tab_name (조인)
        private String trialTitle;
        private String trialSubtitle;
        private String trialTimePoint;
        private String trialPart;
        private String trialResultType;            // "수치", "이미지", "수치+이미지"

        // 전/후 이미지 단일 URL (하위 호환용, BEFORE_1 기준)
        private String beforeImageUrl;             // S3 Presigned URL (BEFORE)
        private String afterImageUrl;              // S3 Presigned URL (AFTER)

        // 다중 이미지 쌍
        private List<ImagePair> imagePairs;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     * 상세 조회용 Response DTO (전체 정보)
     * - 조회수 없음 (유효성 평가 페이지 특성)
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id;
        private Long keywordGroupId;
        private String tabCode;                    // trial_keyword_group.tab_code (조인)
        private String groupName;                  // trial_keyword_group.group_name (탭명과 동일)
        private String tabName;                    // trial_keyword_group.tab_name (조인)

        // 시험 기본 정보
        private String trialTitle;
        private String trialSubtitle;
        private String trialDescription;
        private String trialPersonnel;
        private String trialRequiredSample;
        private String trialReportDate;
        private String trialReportDateSub;
        private String trialTimePoint;
        private String trialPart;
        private String trialResultType;            // "수치", "이미지", "수치+이미지"

        // 전/후 이미지 (상세 페이지용)
        private String beforeImageUrl;             // S3 Presigned URL (BEFORE)
        private String afterImageUrl;              // S3 Presigned URL (AFTER)

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     * 간단 조회용 Response DTO (드롭다운용)
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {
        private Long id;
        private String trialTitle;
    }
}