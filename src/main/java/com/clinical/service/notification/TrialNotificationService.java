package com.clinical.service.notification;

import com.clinical.dto.file.FileRequestDTO;
import com.clinical.dto.notification.TrialNotificationRequestDTO;
import com.clinical.dto.notification.TrialNotificationResponseDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.notification.TrialNotificationEntity;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.repository.notification.TrialNotificationRepository;
import com.clinical.mapper.notification.TrialNotificationMapper;
import com.clinical.service.file.FileService;
import com.clinical.service.user.UserService;
import com.clinical.service.viewCount.ViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrialNotificationService {

    private final TrialNotificationRepository notificationRepository;
    private final TrialNotificationMapper notificationMapper;
    private final FileService fileService;
    private final ViewCountService viewCountService;
    private final UserService userService;

    @Transactional
    public TrialNotificationResponseDTO createNotification(TrialNotificationRequestDTO dto) {
        TrialNotificationEntity notification = notificationMapper.toEntity(dto);
        TrialNotificationEntity saved = notificationRepository.save(notification);

        List<FileResponseDTO> fileResponses = null;
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            fileResponses = fileService.getMultipleUploadLinks(
                    saved.getId(),
                    EntityType.TRIAL_NOTIFICATION,
                    EntityCategory.ATTACHMENT,
                    dto.getUserId(),
                    dto.getFiles());
        }

        return notificationMapper.toDto(saved, fileResponses);
    }

    public List<TrialNotificationResponseDTO> getNotifications() {
        List<TrialNotificationEntity> finds = notificationRepository.findAll();

        return finds.stream().map(find -> {
            TrialNotificationResponseDTO dto = notificationMapper.toDto(find, null);

            if (find.getUserId() == null) {
                dto.setUserName("관리자");
            } else {
                try {
                    String userName = userService.getUserInfo(find.getUserId()).getName();
                    dto.setUserName(userName);
                } catch (Exception e) {
                    dto.setUserName("관리자");
                }
            }

            return dto;
        }).toList();
    }

    public List<TrialNotificationResponseDTO> getIsNotTempNotifications() {
        List<TrialNotificationEntity> finds = notificationRepository.findAllNotTempOrderByNotificationAndDate();

        return finds.stream().map(find -> {
            TrialNotificationResponseDTO dto = notificationMapper.toDto(find, null);

            if (find.getUserId() == null) {
                dto.setUserName("관리자");
            } else {
                try {
                    String userName = userService.getUserInfo(find.getUserId()).getName();
                    dto.setUserName(userName);
                } catch (Exception e) {
                    dto.setUserName("관리자");
                }
            }

            try {
                dto.setView(viewCountService.getViewCount(EntityType.TRIAL_NOTIFICATION.toString(), find.getId()));
            } catch (Exception e) {
                dto.setView(0);
            }

            try {
                List<FileResponseDTO> files = fileService.getFilesByEntity(EntityType.TRIAL_NOTIFICATION, find.getId());
                dto.setAttachedFiles(files);
            } catch (Exception e) {
                dto.setAttachedFiles(null);
            }

            return dto;
        }).toList();
    }

    public List<TrialNotificationResponseDTO> searchNotifications(String field, String keyword) {
        List<TrialNotificationEntity> finds;

        switch (field) {
            case "subject":
                finds = notificationRepository.searchByTitle(keyword);
                break;
            case "content":
                finds = notificationRepository.searchByContent(keyword);
                break;
            case "all":
                finds = notificationRepository.searchByAll(keyword);
                break;
            default:
                finds = notificationRepository.findAllNotTempOrderByNotificationAndDate();
        }

        return finds.stream().map(find -> {
            TrialNotificationResponseDTO dto = notificationMapper.toDto(find, null);

            if (find.getUserId() == null) {
                dto.setUserName("관리자");
            } else {
                try {
                    String userName = userService.getUserInfo(find.getUserId()).getName();
                    dto.setUserName(userName);
                } catch (Exception e) {
                    dto.setUserName("관리자");
                }
            }

            try {
                dto.setView(viewCountService.getViewCount(EntityType.TRIAL_NOTIFICATION.toString(), find.getId()));
            } catch (Exception e) {
                dto.setView(0);
            }

            try {
                List<FileResponseDTO> files = fileService.getFilesByEntity(EntityType.TRIAL_NOTIFICATION, find.getId());
                dto.setAttachedFiles(files);
            } catch (Exception e) {
                dto.setAttachedFiles(null);
            }

            return dto;
        }).toList();
    }

    @Transactional
    public TrialNotificationResponseDTO getNotificationDetail(Long id) {
        TrialNotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 공지사항을 찾을 수 없습니다. ID: " + id));

        try {
            viewCountService.incrementViewCount(EntityType.TRIAL_NOTIFICATION.toString(), id);
        } catch (Exception e) {
            // 조회수 증가 실패해도 계속 진행
        }

        List<FileResponseDTO> files = null;
        try {
            files = fileService.getFilesByEntity(EntityType.TRIAL_NOTIFICATION, id);
        } catch (Exception e) {
            files = null;
        }

        TrialNotificationResponseDTO dto = notificationMapper.toDto(notification, files);

        try {
            dto.setView(viewCountService.getViewCount(EntityType.TRIAL_NOTIFICATION.toString(), id));
        } catch (Exception e) {
            dto.setView(0);
        }

        if (notification.getUserId() == null) {
            dto.setUserName("관리자");
        } else {
            try {
                String userName = userService.getUserInfo(notification.getUserId()).getName();
                dto.setUserName(userName);
            } catch (Exception e) {
                dto.setUserName("관리자");
            }
        }

        return dto;
    }

    @Transactional
    public TrialNotificationResponseDTO updateNotification(Long id, TrialNotificationRequestDTO dto) {
        TrialNotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 공지사항이 없습니다."));

        TrialNotificationEntity updatedNotification = notificationMapper.toEntity(dto);
        updatedNotification.setId(notification.getId());
        notificationRepository.save(updatedNotification);

        List<FileResponseDTO> files = null;

        List<FileRequestDTO> removes = dto.getRemoveFiles();
        if (removes != null && !removes.isEmpty()) {
            for (FileRequestDTO remove : removes) {
                fileService.delete(remove.getId());
            }
        }

        List<FileRequestDTO> adds = dto.getFiles();
        if (adds != null && !adds.isEmpty()) {
            files = fileService.getMultipleUploadLinks(id, EntityType.TRIAL_NOTIFICATION, EntityCategory.ATTACHMENT, dto.getUserId(), adds);
        }

        return notificationMapper.toDto(updatedNotification, files);
    }

    @Transactional
    public void deleteNotification(Long id) {
        try {
            fileService.deleteAllByEntity(EntityType.TRIAL_NOTIFICATION, id);
        } catch (Exception e) {
            // 파일 삭제 실패해도 계속 진행
        }

        notificationRepository.deleteById(id);
    }
}