package com.clinical.dto.trialRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TrialAnswerResponseDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private Long questionId;
        private Long userId;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private String userName;
        private String userPosition;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id;
        private Long questionId;
        private Long userId;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private String userName;
        private String userPosition;
        private String userEmail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {
        private Long id;
        private Long questionId;
        private String content;
    }
}