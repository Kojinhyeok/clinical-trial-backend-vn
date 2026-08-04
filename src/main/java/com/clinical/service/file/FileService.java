package com.clinical.service.file;

import com.clinical.dto.file.FileRequestDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.file.FileEntity;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.mapper.file.FileMapper;
import com.clinical.repository.file.FileRepository;
import com.clinical.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

        private final S3Service s3Service;
        private final FileRepository fileRepository;
        private final FileMapper fileMapper;

        @Value("${spring.cloud.aws.s3.bucket}")
        private String bucket;

        public FileResponseDTO getUploadLink(long entityId, EntityType entityType, EntityCategory entityCategory,
                        Long uploadedBy, String originalFilename, String mimeType, Long fileSize) {

                String s3Key = entityType.name().toLowerCase() + "/" + UUID.randomUUID() + "_" + originalFilename;

                String uploadUrl = s3Service.getPresignedUploadUrl(s3Key, mimeType);

                FileRequestDTO requestDto = new FileRequestDTO();
                requestDto.setEntityType(entityType.toString());
                requestDto.setEntityId(entityId);
                requestDto.setFileCategory(entityCategory.toString());
                requestDto.setOriginalFilename(originalFilename);
                requestDto.setMimeType(mimeType);
                requestDto.setFileSize(fileSize);
                requestDto.setUploadedBy(uploadedBy);

                FileEntity entity = fileMapper.toEntity(requestDto, s3Key, bucket);
                FileEntity savedEntity = fileRepository.save(entity);

                FileResponseDTO response = fileMapper.toDto(savedEntity);
                response.setUploadUrl(uploadUrl);

                return response;
        }

        public List<FileResponseDTO> getMultipleUploadLinks(long entityId, EntityType entityType,
                        EntityCategory entityCategory,
                        Long uploadedBy, List<FileRequestDTO> fileRequests) {

                if (fileRequests == null || fileRequests.isEmpty()) {
                        return Collections.emptyList();
                }

                return fileRequests.stream()
                                .map(req -> getUploadLink(
                                                entityId,
                                                entityType,
                                                entityCategory,
                                                uploadedBy,
                                                req.getOriginalFilename(),
                                                req.getMimeType(),
                                                req.getFileSize()))
                                .collect(Collectors.toList());
        }

        public List<FileResponseDTO> getFilesByEntityType(EntityType entityType) {
                List<FileEntity> entities = fileRepository.findAllByEntityType(entityType.toString());
                return entities.stream()
                                .map(entity -> {
                                        FileResponseDTO dto = fileMapper.toDto(entity);
                                        String presignedUrl = s3Service.getPresignedDownloadUrl(entity.getS3Key(),
                                                        dto.getOriginalFilename());
                                        dto.setS3Url(presignedUrl);
                                        return dto;
                                })
                                .collect(Collectors.toList());
        }

        public List<FileResponseDTO> getFilesByEntity(EntityType entityType, Long entityId) {
                List<FileEntity> entities = fileRepository.findAllByEntityTypeAndEntityId(
                                entityType.toString(), entityId);
                return entities.stream()
                                .map(entity -> {
                                        FileResponseDTO dto = fileMapper.toDto(entity);
                                        String presignedUrl = s3Service.getPresignedDownloadUrl(entity.getS3Key(),
                                                        dto.getOriginalFilename());
                                        dto.setS3Url(presignedUrl);
                                        return dto;
                                })
                                .collect(Collectors.toList());
        }

        public List<FileResponseDTO> getFilesByEntityAndCategory(EntityType entityType, Long entityId,
                        EntityCategory entityCategory) {
                List<FileEntity> entities = fileRepository.findAllByEntityTypeAndEntityIdAndFileCategory(
                                entityType.toString(), entityId, entityCategory.toString());
                return entities.stream()
                                .map(entity -> {
                                        FileResponseDTO dto = fileMapper.toDto(entity);
                                        String presignedUrl = s3Service.getPresignedDownloadUrl(entity.getS3Key(),
                                                        dto.getOriginalFilename());
                                        dto.setS3Url(presignedUrl);
                                        return dto;
                                })
                                .collect(Collectors.toList());
        }

        public FileResponseDTO getUpdateLink(EntityType entityType, Long entityId, EntityCategory entityCategory,
                        Long uploadedBy, String newOriginalFilename, String newMimeType, Long newFileSize) {

                List<FileEntity> existingFiles = fileRepository.findAllByEntityTypeAndEntityIdAndFileCategory(
                                entityType.toString(), entityId, entityCategory.toString());

                if (existingFiles != null && !existingFiles.isEmpty()) {
                        for (FileEntity file : existingFiles) {
                                s3Service.deleteFile(file.getS3Key());
                                fileRepository.delete(file);
                        }
                        fileRepository.flush();
                }

                return getUploadLink(entityId, entityType, entityCategory, uploadedBy, newOriginalFilename, newMimeType,
                                newFileSize);
        }

        public void delete(Long fileId) {
                FileEntity entity = fileRepository.findById(fileId)
                                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId));

                s3Service.deleteFile(entity.getS3Key());

                fileRepository.delete(entity);
        }

        public void linkFileToEntity(Long fileId, Long entityId, Long uploadedBy) {
                FileEntity entity = fileRepository.findById(fileId)
                                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다. ID: " + fileId));
                entity.setEntityId(entityId);
                if (uploadedBy != null) {
                        entity.setUploadedBy(uploadedBy);
                }
                fileRepository.save(entity);
        }

        public void deleteAllByEntity(EntityType entityType, Long entityId) {
                List<FileEntity> files = fileRepository.findAllByEntityTypeAndEntityId(entityType.toString(), entityId);
                for (FileEntity file : files) {
                        s3Service.deleteFile(file.getS3Key());
                        fileRepository.delete(file);
                }
        }

        public FileResponseDTO getUploadLink(long entityId, EntityType entityType, String fileCategory,
                Long uploadedBy, String originalFilename, String mimeType, Long fileSize) {

        String s3Key = entityType.name().toLowerCase() + "/" + UUID.randomUUID() + "_" + originalFilename;
        String uploadUrl = s3Service.getPresignedUploadUrl(s3Key, mimeType);

        FileRequestDTO requestDto = new FileRequestDTO();
        requestDto.setEntityType(entityType.toString());
        requestDto.setEntityId(entityId);
        requestDto.setFileCategory(fileCategory);
        requestDto.setOriginalFilename(originalFilename);
        requestDto.setMimeType(mimeType);
        requestDto.setFileSize(fileSize);
        requestDto.setUploadedBy(uploadedBy);

        FileEntity entity = fileMapper.toEntity(requestDto, s3Key, bucket);
        FileEntity savedEntity = fileRepository.save(entity);

        FileResponseDTO response = fileMapper.toDto(savedEntity);
        response.setUploadUrl(uploadUrl);
        return response;
        }

        public List<FileResponseDTO> getFilesByEntityAndCategory(EntityType entityType, Long entityId,
                String fileCategory) {
        List<FileEntity> entities = fileRepository.findAllByEntityTypeAndEntityIdAndFileCategory(
                entityType.toString(), entityId, fileCategory);
        return entities.stream()
                .map(entity -> {
                        FileResponseDTO dto = fileMapper.toDto(entity);
                        String presignedUrl = s3Service.getPresignedDownloadUrl(entity.getS3Key(),
                                dto.getOriginalFilename());
                        dto.setS3Url(presignedUrl);
                        return dto;
                })
                .collect(Collectors.toList());
        }

        public List<FileResponseDTO> getFilesByEntityTypeAndEntityIds(
                EntityType entityType, List<Long> entityIds) {
        List<FileEntity> entities = fileRepository
                .findAllByEntityTypeAndEntityIdIn(entityType.toString(), entityIds);
        return entities.stream()
                .map(entity -> {
                        FileResponseDTO dto = fileMapper.toDto(entity);
                        dto.setS3Url(s3Service.getPresignedDownloadUrl(
                                entity.getS3Key(), dto.getOriginalFilename()));
                        return dto;
                })
                .collect(Collectors.toList());
        }
}