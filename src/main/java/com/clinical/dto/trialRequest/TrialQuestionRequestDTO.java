package com.clinical.dto.trialRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class TrialQuestionRequestDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String title;
        private String companyName;
        private String companyId;
        private String representativeName;
        private String representativePosition;
        private String phone;
        private String email;
        private String productType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String password;
        private String contactType;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String companyName;
        private String companyId;
        private String representativeName;
        private String representativePosition;
        private String phone;
        private String email;
        private String productType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String contactType;
        private String content;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordCheck {
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAnswer {
        private Long userId;
        private String content;
    }
}