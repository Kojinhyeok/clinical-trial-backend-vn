package com.clinical.service.trial;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.trialInvitro.TrialInvitroRequestDTO;
import com.clinical.dto.trialInvitro.TrialInvitroResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.trial.TrialInvitro;
import com.clinical.mapper.trialInvitro.TrialInvitroMapper;
import com.clinical.repository.trial.TrialInvitroRepository;
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
public class TrialInvitroServiceImpl implements TrialInvitroService {

    private final TrialInvitroRepository repository;
    private final TrialInvitroMapper mapper;
    private final FileService fileService;

    @Override
    public List<TrialInvitroResponseDTO> getAllItems() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(entity -> {
                    TrialInvitroResponseDTO dto = mapper.toResponseDTO(entity);
                    dto.setThumbnailUrl(getThumbnailUrl(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TrialInvitroResponseDTO getItemById(Long id) {
        TrialInvitro entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("항목을 찾을 수 없습니다. ID: " + id));
        TrialInvitroResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setThumbnailUrl(getThumbnailUrl(entity.getId()));
        return dto;
    }

    @Override
    @Transactional
    public TrialInvitroResponseDTO saveItem(TrialInvitroRequestDTO dto, Long userId) {
        TrialInvitro entity = mapper.toEntity(dto);
        TrialInvitro savedEntity = repository.save(entity);

        String uploadUrl = handleFileUpload(savedEntity.getId(), dto, userId);

        TrialInvitroResponseDTO response = mapper.toResponseDTO(savedEntity);
        response.setUploadUrl(uploadUrl);
        return response;
    }

    @Override
    @Transactional
    public TrialInvitroResponseDTO updateItem(TrialInvitroRequestDTO dto, Long userId) {
        TrialInvitro entity = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("항목을 찾을 수 없습니다. ID: " + dto.getId()));

        applyUpdate(entity, dto);

        String uploadUrl = handleFileUpload(entity.getId(), dto, userId);

        TrialInvitroResponseDTO response = mapper.toResponseDTO(entity);
        response.setUploadUrl(uploadUrl);
        return response;
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        fileService.deleteAllByEntity(EntityType.TRIAL_INVITRO, id);
        repository.deleteById(id);
    }

    private String handleFileUpload(Long entityId, TrialInvitroRequestDTO dto, Long userId) {
        if (dto.getOriginalFilename() != null && !dto.getOriginalFilename().isEmpty()) {
            if (dto.getId() != null) {
                fileService.deleteAllByEntity(EntityType.TRIAL_INVITRO, entityId);
            }
            FileResponseDTO fileInfo = fileService.getUploadLink(
                    entityId,
                    EntityType.TRIAL_INVITRO,
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

    private String getThumbnailUrl(Long entityId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.TRIAL_INVITRO, entityId, EntityCategory.THUMBNAIL);
            if (files != null && !files.isEmpty()) {
                return files.get(0).getS3Url();
            }
        } catch (Exception e) {
            // 파일 정보가 없을 경우 무시
        }
        return null;
    }

    private void applyUpdate(TrialInvitro entity, TrialInvitroRequestDTO dto) {
        entity.setTitle(dto.getTitle());
        entity.setDetail(dto.getDetail());
        entity.setTrialOutput(dto.getTrialOutput());
    }
}