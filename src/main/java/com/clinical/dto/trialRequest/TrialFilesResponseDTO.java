package com.clinical.dto.trialRequest;

import com.clinical.dto.file.FileResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialFilesResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    @JsonProperty("isTemp")
    private Boolean isTemp;
    @JsonProperty("isNotification")
    private Boolean isNotification;
    private String title;
    private String content;
    private List<FileResponseDTO> attachedFiles;
    private Integer view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}