package com.clinical.service.trial;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.trialSafety.TrialSafetyRequestDTO;
import com.clinical.dto.trialSafety.TrialSafetyResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.trial.TrialSafety;
import com.clinical.mapper.trialSafety.TrialSafetyMapper;
import com.clinical.repository.trial.TrialSafetyRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrialSafetyServiceImpl implements TrialSafetyService {

    private final TrialSafetyRepository repository;
    private final TrialSafetyMapper mapper;
    private final FileService fileService;

    /**
     * 전체 목록 조회 (노출 상태 체크 제거)
     */
    @Override
    public List<TrialSafetyResponseDTO> getAllItems() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(entity -> {
                    TrialSafetyResponseDTO dto = mapper.toResponseDTO(entity);
                    dto.setThumbnailUrl(getThumbnailUrl(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 신규 등록
     */
    @Override
    @Transactional
    public TrialSafetyResponseDTO saveItem(TrialSafetyRequestDTO dto, Long userId) {
        // 1. 엔티티 저장
        TrialSafety entity = mapper.toEntity(dto);
        TrialSafety savedEntity = repository.save(entity);

        // 2. 파일 정보가 있으면 파일 서비스에 userId 전달하여 업로드 URL 발급
        String uploadUrl = handleFileUpload(savedEntity.getId(), dto, userId);

        TrialSafetyResponseDTO response = mapper.toResponseDTO(savedEntity);
        response.setUploadUrl(uploadUrl);
        return response;
    }

    /**
     * 정보 수정
     */
    @Override
    @Transactional
    public TrialSafetyResponseDTO updateItem(TrialSafetyRequestDTO dto, Long userId) {
        TrialSafety entity = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("항목을 찾을 수 없습니다. ID: " + dto.getId()));

        // 1. 기본 정보 수정 (Dirty Checking)
        applyUpdate(entity, dto);

        // 2. 파일 교체 로직 (기존 파일 삭제 후 새 URL 발급)
        String uploadUrl = handleFileUpload(entity.getId(), dto, userId);

        TrialSafetyResponseDTO response = mapper.toResponseDTO(entity);
        response.setUploadUrl(uploadUrl);
        return response;
    }

    /**
     * 항목 삭제
     */
    @Override
    @Transactional
    public void deleteItem(Long id) {
        // 관련 파일(S3/DB) 전체 삭제
        fileService.deleteAllByEntity(EntityType.TRIAL_SAFETY, id);
        repository.deleteById(id);
    }

    /**
     * 단일 항목 상세 조회
     */
    @Override
    public TrialSafetyResponseDTO getItemById(Long id) {
        TrialSafety entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("항목 없음"));
        TrialSafetyResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setThumbnailUrl(getThumbnailUrl(entity.getId()));
        return dto;
    }

    /**
     * 파일 업로드 처리 내부 메서드 (userId는 파일 이력용으로만 사용)
     */
    private String handleFileUpload(Long entityId, TrialSafetyRequestDTO dto, Long userId) {
        if (dto.getOriginalFilename() != null && !dto.getOriginalFilename().isEmpty()) {
            // 수정 시에는 기존 파일 전체 삭제 후 재발급 (중복 방지)
            if (dto.getId() != null) {
                fileService.deleteAllByEntity(EntityType.TRIAL_SAFETY, entityId);
            }

            // 파일 서비스에 정보 전달 및 S3 Pre-signed URL 획득
            FileResponseDTO fileInfo = fileService.getUploadLink(
                    entityId,
                    EntityType.TRIAL_SAFETY,
                    EntityCategory.THUMBNAIL,
                    userId,
                    dto.getOriginalFilename(),
                    dto.getMimeType(),
                    dto.getFileSize()
            );
            return fileInfo.getUploadUrl();
        }
        return null;
    }

    /**
     * S3 썸네일 URL 조회 헬퍼 메서드
     */
    private String getThumbnailUrl(Long entityId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.TRIAL_SAFETY, entityId, EntityCategory.THUMBNAIL);
            if (files != null && !files.isEmpty()) {
                return files.get(0).getS3Url();
            }
        } catch (Exception e) {
            // 파일 정보가 없을 경우 무시
        }
        return null;
    }

    /**
     * 엔티티 필드 업데이트 (수정 시)
     */
    private void applyUpdate(TrialSafety entity, TrialSafetyRequestDTO dto) {
        entity.setTitle(dto.getTitle());
        entity.setTrialPeriod(dto.getTrialPeriod());
        entity.setParticipantCount(dto.getParticipantCount());
        entity.setTrialLocation(dto.getTrialLocation());
        entity.setTrialOutput(dto.getTrialOutput());
        // isActive 관련 세팅 제거됨
    }
}