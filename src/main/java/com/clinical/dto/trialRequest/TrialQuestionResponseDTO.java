package com.clinical.dto.trialRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TrialQuestionResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String title;
        private String representativeName;
        private String status;
        private Boolean hasAnswer;
        private LocalDateTime createdAt;
        private Boolean isProtected;
        private Boolean isAnswer;
        private Long questionId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id;
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
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean hasAnswer;
        private List<AnswerResponse> answers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerResponse {
        private Long id;
        private Long questionId;
        private Long userId;
        private String userName;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {
        private Long id;
        private String title;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordCheckResponse {
        private Boolean isValid;
        private String message;
    }
}