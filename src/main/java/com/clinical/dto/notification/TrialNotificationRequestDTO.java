package com.clinical.dto.notification;

import com.clinical.dto.file.FileRequestDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrialNotificationRequestDTO {
    private String title;
    private String content;
    private Long userId;
    private Boolean isNotification;
    private Boolean isTemp;
    private List<FileRequestDTO> files;
    private List<FileRequestDTO> removeFiles;
}