package com.clinical.dto.notification;

import com.clinical.dto.file.FileResponseDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TrialNotificationResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private Boolean isNotification;
    private Boolean isTemp;
    private int view;

    private List<FileResponseDTO> attachedFiles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}