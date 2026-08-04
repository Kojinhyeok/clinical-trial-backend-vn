package com.clinical.mapper.irb;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.irb.IrbTestRequestDTO;
import com.clinical.dto.irb.IrbTestResponseDTO;
import com.clinical.entity.irb.IrbTestEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class IrbTestMapper {

    /**
     * Entity -> DTO 변환
     */
    public IrbTestResponseDTO toDto(IrbTestEntity entity, List<FileResponseDTO> attachedFiles) {
        if (entity == null) return null;

        IrbTestResponseDTO dto = new IrbTestResponseDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setIsTemp(entity.getIsTemp());
        dto.setIrbTestId(entity.getIrbTestId());
        dto.setIrbTestIdRef(entity.getIrbTestIdRef());
        dto.setDepth(entity.getDepth());
        dto.setIrbCode(entity.getIrbCode());
        dto.setStatus(entity.getStatus());
        dto.setCategoryId(entity.getCategoryId());
        dto.setTitle(entity.getTitle());
        dto.setAttachedFiles(attachedFiles);
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    /**
     * DTO -> Entity 변환 (주로 등록/수정 시 사용)
     */
    public IrbTestEntity toEntity(IrbTestRequestDTO dto) {
        if (dto == null) return null;

        return IrbTestEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .isTemp(dto.getIsTemp())
                .irbTestId(dto.getIrbTestId())
                .irbTestIdRef(dto.getIrbTestIdRef())
                .depth(dto.getDepth())
                .irbCode(dto.getIrbCode())
                .status(dto.getStatus().toString())
                .categoryId(dto.getCategoryId())
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }


}