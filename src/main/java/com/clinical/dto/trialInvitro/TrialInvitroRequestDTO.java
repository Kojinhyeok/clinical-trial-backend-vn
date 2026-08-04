package com.clinical.dto.trialInvitro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrialInvitroRequestDTO {
    private Long id; // 수정 시에는 필요, 등록 시에는 null
    private String title;
    private String detail;
    private String trialOutput;
    private String originalFilename;
    private String mimeType;
    private Long fileSize;
}