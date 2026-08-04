package com.clinical.mapper.file;

import com.clinical.entity.file.FileEntity;
import com.clinical.dto.file.FileRequestDTO;
import com.clinical.dto.file.FileResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileEntity toEntity(FileRequestDTO dto, String s3Key, String s3Bucket) {
        if (dto == null)
            return null;

        return FileEntity.builder()
                .entityType(dto.getEntityType())
                .entityId(dto.getEntityId())
                .fileCategory(dto.getFileCategory())
                .uploadedBy(dto.getUploadedBy())
                .originalFilename(dto.getOriginalFilename())
                .fileSize(dto.getFileSize())
                .mimeType(dto.getMimeType())
                .s3Key(s3Key)
                .s3Bucket(s3Bucket)
                .build();
    }

    public FileResponseDTO toDto(FileEntity entity) {
        if (entity == null)
            return null;

        return FileResponseDTO.builder()
                .id(entity.getId())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .fileCategory(entity.getFileCategory())
                .originalFilename(entity.getOriginalFilename())
                .fileSize(entity.getFileSize())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}