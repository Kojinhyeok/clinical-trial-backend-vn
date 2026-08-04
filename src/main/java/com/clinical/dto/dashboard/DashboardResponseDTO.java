package com.clinical.dto.dashboard;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    // 통계 카드
    private long totalMembers;
    private long irbUnderReview;
    private long activeRecruitments;
    private long permanentlyOpenRecruitments;
    private long unansweredInquiries;

    // 누적 데이터
    private List<CumulativeItem> cumulativeData;

    // 최근 활동
    private List<RecentIrbItem> recentIrbs;
    private List<RecentApplicationItem> recentApplications;
    private List<RecentInquiryItem> recentInquiries;
    private List<PopupItem> activePopups;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CumulativeItem {
        private Long id;
        private String dataType;
        private Long dataCount;
        private String imageUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentIrbItem {
        private Long id;
        private String title;
        private String categoryName;
        private String status;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentApplicationItem {
        private Long id;
        private String applicantName;
        private String trialName;
        private String status;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentInquiryItem {
        private Long id;
        private String title;
        private String status;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupItem {
        private Long id;
        private String title;
        private Boolean isActive;
        private Integer popupOrder;
    }
}
