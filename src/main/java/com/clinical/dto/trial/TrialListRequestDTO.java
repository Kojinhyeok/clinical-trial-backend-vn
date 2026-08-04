package com.clinical.dto.trial;

import lombok.*;

/**
 * TrialList Request DTO
 */
public class TrialListRequestDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private Long keywordGroupId;
        private String trialTitle;
        private String trialSubtitle;
        private String trialDescription;
        private String trialPersonnel;
        private String trialRequiredSample;
        private String trialReportDate;
        private String trialReportDateSub;   
        private String trialTimePoint;
        private String trialPart;
        private String trialResultType;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private Long keywordGroupId;
        private String trialTitle;
        private String trialSubtitle;
        private String trialDescription;
        private String trialPersonnel;
        private String trialRequiredSample;
        private String trialReportDate;
        private String trialReportDateSub;    
        private String trialTimePoint;
        private String trialPart;
        private String trialResultType;
    }
}