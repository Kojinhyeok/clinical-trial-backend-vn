package com.clinical.dto.trialSafety;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class TrialSafetyRequestDTO {
    private Long id; // 수정 시에는 필요, 등록 시에는 null
    private String title;
    private String trialPeriod;
    private String participantCount;
    private String trialLocation;
    private String trialOutput;
    private String originalFilename;
    private String mimeType;
    private Long fileSize;
}
