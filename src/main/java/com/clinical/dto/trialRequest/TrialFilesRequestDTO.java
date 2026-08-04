package com.clinical.dto.trialRequest;

import com.clinical.dto.file.FileRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialFilesRequestDTO {
    private Long userId;
    @JsonProperty("isTemp")
    private Boolean isTemp;
    @JsonProperty("isNotification")
    private Boolean isNotification;
    private String title;
    private String content;
    private List<FileRequestDTO> files;
    private List<FileRequestDTO> removeFiles;
}