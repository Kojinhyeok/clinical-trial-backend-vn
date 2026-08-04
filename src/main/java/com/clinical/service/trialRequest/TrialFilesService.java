package com.clinical.service.trialRequest;

import com.clinical.dto.file.FileRequestDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.trialRequest.TrialFilesRequestDTO;
import com.clinical.dto.trialRequest.TrialFilesResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.trialRequest.TrialFiles;
import com.clinical.repository.trialRequest.TrialFilesRepository;
import com.clinical.service.file.FileService;
import com.clinical.service.user.UserService;
import com.clinical.service.viewCount.ViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrialFilesService {

    private final TrialFilesRepository trialFilesRepository;
    private final FileService fileService;
    private final ViewCountService viewCountService;
    private final UserService userService;

    public List<TrialFilesResponseDTO> getAllFilesForAdmin() {
        List<TrialFiles> files = trialFilesRepository.findAllByOrderByCreatedAtDesc();
        return files.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<TrialFilesResponseDTO> getAllFiles() {
        List<TrialFiles> files = trialFilesRepository.findByIsTempOrderByCreatedAtDesc(false);

        List<TrialFiles> sortedFiles = files.stream()
                .sorted((a, b) -> {
                    if (a.getIsNotification() && !b.getIsNotification()) return -1;
                    if (!a.getIsNotification() && b.getIsNotification()) return 1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .collect(Collectors.toList());

        return sortedFiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public TrialFilesResponseDTO getFileDetail(Long id) {
        TrialFiles file = trialFilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 자료를 찾을 수 없습니다. ID: " + id));

        try {
            viewCountService.incrementViewCount(EntityType.TRIAL_FILE.toString(), id);
        } catch (Exception e) {
            // 조회수 증가 실패해도 계속 진행
        }

        return convertToDto(file);
    }

    @Transactional
    public TrialFilesResponseDTO createFile(TrialFilesRequestDTO dto) {
        TrialFiles file = TrialFiles.builder()
                .userId(dto.getUserId())
                .isTemp(dto.getIsTemp())
                .isNotification(dto.getIsNotification())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        TrialFiles savedFile = trialFilesRepository.save(file);

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            fileService.getMultipleUploadLinks(
                    savedFile.getId(),
                    EntityType.TRIAL_FILE,
                    EntityCategory.ATTACHMENT,
                    dto.getUserId(),
                    dto.getFiles()
            );
        }

        return convertToDto(savedFile);
    }

    @Transactional
    public TrialFilesResponseDTO updateFile(Long id, TrialFilesRequestDTO dto) {
        TrialFiles file = trialFilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 자료를 찾을 수 없습니다."));

        file.setUserId(dto.getUserId());
        file.setIsTemp(dto.getIsTemp());
        file.setIsNotification(dto.getIsNotification());
        file.setTitle(dto.getTitle());
        file.setContent(dto.getContent());

        if (dto.getRemoveFiles() != null && !dto.getRemoveFiles().isEmpty()) {
            for (FileRequestDTO removeFile : dto.getRemoveFiles()) {
                fileService.delete(removeFile.getId());
            }
        }

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            fileService.getMultipleUploadLinks(
                    id,
                    EntityType.TRIAL_FILE,
                    EntityCategory.ATTACHMENT,
                    dto.getUserId(),
                    dto.getFiles()
            );
        }

        return convertToDto(file);
    }

    @Transactional
    public void deleteFile(Long id) {
        try {
            fileService.deleteAllByEntity(EntityType.TRIAL_FILE, id);
        } catch (Exception e) {
            // 파일 삭제 실패해도 계속 진행
        }

        trialFilesRepository.deleteById(id);
    }

    public List<TrialFilesResponseDTO> searchByTitle(String keyword) {
        List<TrialFiles> files = trialFilesRepository.findByIsTempOrderByCreatedAtDesc(false);

        return files.stream()
                .filter(file -> file.getTitle().contains(keyword))
                .sorted((a, b) -> {
                    if (a.getIsNotification() && !b.getIsNotification()) return -1;
                    if (!a.getIsNotification() && b.getIsNotification()) return 1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TrialFilesResponseDTO> searchByContent(String keyword) {
        List<TrialFiles> files = trialFilesRepository.findByIsTempOrderByCreatedAtDesc(false);

        return files.stream()
                .filter(file -> file.getContent() != null && file.getContent().contains(keyword))
                .sorted((a, b) -> {
                    if (a.getIsNotification() && !b.getIsNotification()) return -1;
                    if (!a.getIsNotification() && b.getIsNotification()) return 1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TrialFilesResponseDTO convertToDto(TrialFiles file) {
        List<FileResponseDTO> attachedFiles = null;
        try {
            attachedFiles = fileService.getFilesByEntity(EntityType.TRIAL_FILE, file.getId());
        } catch (Exception e) {
            // 첨부파일 없음
        }

        int viewCount = 0;
        try {
            viewCount = viewCountService.getViewCount(EntityType.TRIAL_FILE.toString(), file.getId());
        } catch (Exception e) {
            // 조회수 조회 실패
        }

        String userName = "관리자";
        if (file.getUserId() != null) {
            try {
                userName = userService.getUserInfo(file.getUserId()).getName();
            } catch (Exception e) {
                // 작성자 조회 실패
            }
        }

        return TrialFilesResponseDTO.builder()
                .id(file.getId())
                .userId(file.getUserId())
                .userName(userName)
                .isTemp(file.getIsTemp())
                .isNotification(file.getIsNotification())
                .title(file.getTitle())
                .content(file.getContent())
                .attachedFiles(attachedFiles)
                .view(viewCount)
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}