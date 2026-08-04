package com.clinical.service.coreMember;

import com.clinical.dto.coreMember.CoreMemberRequestDTO;
import com.clinical.dto.coreMember.CoreMemberResponseDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.coreMember.CoreMemberEntity;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.repository.coreMember.CoreMemberRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoreMemberService {

    private final CoreMemberRepository coreMemberRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public List<CoreMemberResponseDTO> getAllCoreMembers() {
        return coreMemberRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CoreMemberResponseDTO getCoreMemberById(Long id) {
        CoreMemberEntity entity = coreMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("핵심 인력을 찾을 수 없습니다: " + id));
        return toResponseDTO(entity);
    }

    @Transactional
    public CoreMemberResponseDTO createCoreMember(CoreMemberRequestDTO.Create request) {
        CoreMemberEntity entity = CoreMemberEntity.builder()
                .name(request.getName())
                .affiliation(request.getAffiliation())
                .position(request.getPosition())
                .slogan(request.getSlogan())
                .detail(request.getDetail())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        CoreMemberEntity saved = coreMemberRepository.save(entity);
        return toResponseDTO(saved);
    }

    @Transactional
    public CoreMemberResponseDTO updateCoreMember(Long id, CoreMemberRequestDTO.Update request) {
        CoreMemberEntity entity = coreMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("핵심 인력을 찾을 수 없습니다: " + id));

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getAffiliation() != null) entity.setAffiliation(request.getAffiliation());
        if (request.getPosition() != null) entity.setPosition(request.getPosition());
        if (request.getSlogan() != null) entity.setSlogan(request.getSlogan());
        if (request.getDetail() != null) entity.setDetail(request.getDetail());
        if (request.getDisplayOrder() != null) entity.setDisplayOrder(request.getDisplayOrder());

        CoreMemberEntity updated = coreMemberRepository.save(entity);
        return toResponseDTO(updated);
    }

    @Transactional
    public void deleteCoreMember(Long id) {
        if (!coreMemberRepository.existsById(id)) {
            throw new RuntimeException("핵심 인력을 찾을 수 없습니다: " + id);
        }
        coreMemberRepository.deleteById(id);
    }

    private CoreMemberResponseDTO toResponseDTO(CoreMemberEntity entity) {
        String imageUrl = getImageUrl(entity.getId());

        return CoreMemberResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .affiliation(entity.getAffiliation())
                .position(entity.getPosition())
                .slogan(entity.getSlogan())
                .detail(entity.getDetail())
                .displayOrder(entity.getDisplayOrder())
                .imageUrl(imageUrl)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private String getImageUrl(Long entityId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.CORE_MEMBER, entityId, EntityCategory.THUMBNAIL
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