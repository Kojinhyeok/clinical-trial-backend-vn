package com.clinical.dto.trialSafety;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrialSafetyResponseDTO {
    private Long id;
    private String title;
    private String trialPeriod;
    private String participantCount;
    private String trialLocation;
    private String trialOutput;
    private String createdAt;
    private String thumbnailUrl; // 조회용 S3 경로
    private String uploadUrl; // 등록/수정 시 파일 전송용 S3 발급 경로
}
