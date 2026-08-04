package com.clinical.service.popup;

import com.clinical.dto.popup.PopupRequestDTO;
import com.clinical.dto.popup.PopupResponseDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.popup.PopupEntity;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.repository.popup.PopupRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public List<PopupResponseDTO> getActivePopups() {
        return popupRepository.findByIsActiveTrueOrderByPopupOrderAsc().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PopupResponseDTO> getAllPopups() {
        return popupRepository.findAllByOrderByPopupOrderAsc().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PopupResponseDTO getPopupById(Long id) {
        PopupEntity entity = popupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("팝업을 찾을 수 없습니다: " + id));
        return toResponseDTO(entity);
    }

    @Transactional
    public PopupResponseDTO createPopup(PopupRequestDTO.Create request) {
        PopupEntity entity = PopupEntity.builder()
                .title(request.getTitle())
                .popupOrder(request.getPopupOrder() != null ? request.getPopupOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .linkUrl(request.getLinkUrl())
                .build();

        PopupEntity saved = popupRepository.save(entity);
        return toResponseDTO(saved);
    }

    @Transactional
    public PopupResponseDTO updatePopup(Long id, PopupRequestDTO.Update request) {
        PopupEntity entity = popupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("팝업을 찾을 수 없습니다: " + id));

        if (request.getTitle() != null) entity.setTitle(request.getTitle());
        if (request.getPopupOrder() != null) entity.setPopupOrder(request.getPopupOrder());
        if (request.getIsActive() != null) entity.setIsActive(request.getIsActive());
        entity.setLinkUrl(request.getLinkUrl());

        PopupEntity updated = popupRepository.save(entity);
        return toResponseDTO(updated);
    }

    @Transactional
    public void deletePopup(Long id) {
        if (!popupRepository.existsById(id)) {
            throw new RuntimeException("팝업을 찾을 수 없습니다: " + id);
        }
        popupRepository.deleteById(id);
    }

    private PopupResponseDTO toResponseDTO(PopupEntity entity) {
        String imageUrl = getPopupImageUrl(entity.getId());

        return PopupResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .imageUrl(imageUrl)
                .linkUrl(entity.getLinkUrl())
                .popupOrder(entity.getPopupOrder())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private String getPopupImageUrl(Long popupId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.POPUP, popupId, EntityCategory.BASIC
            );

            if (files != null && !files.isEmpty()) {
                return files.get(0).getS3Url();
            }
        } catch (Exception e) {
            // 에러 무시
        }
        return null;
    }
}