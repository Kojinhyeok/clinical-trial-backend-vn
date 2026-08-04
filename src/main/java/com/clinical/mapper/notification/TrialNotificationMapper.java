package com.clinical.mapper.notification;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.notification.TrialNotificationRequestDTO;
import com.clinical.dto.notification.TrialNotificationResponseDTO;
import com.clinical.entity.notification.TrialNotificationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrialNotificationMapper {

    public TrialNotificationEntity toEntity(TrialNotificationRequestDTO dto) {
        if (dto == null)
            return null;

        return TrialNotificationEntity.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .isNotification(dto.getIsNotification())
                .isTemp(dto.getIsTemp())
                .build();
    }

    public TrialNotificationResponseDTO toDto(TrialNotificationEntity notification,
            List<FileResponseDTO> attachedFiles) {
        if (notification == null)
            return null;

        return TrialNotificationResponseDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isNotification(notification.isNotification())
                .isTemp(notification.isTemp())
                .attachedFiles(attachedFiles)
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}