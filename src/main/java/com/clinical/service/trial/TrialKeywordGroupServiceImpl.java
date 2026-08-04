package com.clinical.service.trial;

import com.clinical.dto.trial.TrialKeywordGroupRequestDTO;
import com.clinical.dto.trial.TrialKeywordGroupResponseDTO;
import com.clinical.entity.trial.TrialKeywordGroup;
import com.clinical.repository.trial.TrialKeywordGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 시험 키워드 그룹 Service 구현체 (탭 전용, 그룹/추천검색어 미사용)
 */
@Service
@RequiredArgsConstructor
public class TrialKeywordGroupServiceImpl implements TrialKeywordGroupService {

    private final TrialKeywordGroupRepository keywordGroupRepository;
    // searchKeywordRepository 제거 (미사용)

    @Override
    @Transactional
    public TrialKeywordGroupResponseDTO.DetailResponse createKeywordGroup(TrialKeywordGroupRequestDTO.Create request) {
        TrialKeywordGroup keywordGroup = TrialKeywordGroup.builder()
                .tabCode(request.getTabCode())
                .tabName(request.getTabName())
                .groupName(request.getGroupName())
                .displayOrder(request.getDisplayOrder())
                .build();

        TrialKeywordGroup saved = keywordGroupRepository.save(keywordGroup);
        return toDetailResponse(saved);
    }

    @Override
    @Transactional
    public TrialKeywordGroupResponseDTO.DetailResponse updateKeywordGroup(Long id, TrialKeywordGroupRequestDTO.Update request) {
        TrialKeywordGroup keywordGroup = keywordGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("탭을 찾을 수 없습니다. ID: " + id));

        if (request.getTabCode() != null) keywordGroup.setTabCode(request.getTabCode());
        if (request.getTabName() != null) keywordGroup.setTabName(request.getTabName());
        if (request.getGroupName() != null) keywordGroup.setGroupName(request.getGroupName());
        if (request.getDisplayOrder() != null) keywordGroup.setDisplayOrder(request.getDisplayOrder());

        TrialKeywordGroup updated = keywordGroupRepository.save(keywordGroup);
        return toDetailResponse(updated);
    }

    @Override
    @Transactional
    public void deleteKeywordGroup(Long id) {
        if (!keywordGroupRepository.existsById(id)) {
            throw new RuntimeException("탭을 찾을 수 없습니다. ID: " + id);
        }
        keywordGroupRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TrialKeywordGroupResponseDTO.DetailResponse getKeywordGroupById(Long id) {
        TrialKeywordGroup keywordGroup = keywordGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("탭을 찾을 수 없습니다. ID: " + id));
        return toDetailResponse(keywordGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialKeywordGroupResponseDTO.ListResponse> getAllKeywordGroups() {
        return keywordGroupRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialKeywordGroupResponseDTO.TabResponse> getTabs() {
        return keywordGroupRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::toTabResponse)
                .collect(Collectors.toList());
    }

    // ===== Private Helper Methods =====

    private TrialKeywordGroupResponseDTO.ListResponse toListResponse(TrialKeywordGroup entity) {
        return TrialKeywordGroupResponseDTO.ListResponse.builder()
                .id(entity.getId())
                .tabCode(entity.getTabCode())
                .tabName(entity.getTabName())
                .groupName(entity.getGroupName())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private TrialKeywordGroupResponseDTO.DetailResponse toDetailResponse(TrialKeywordGroup entity) {
        return TrialKeywordGroupResponseDTO.DetailResponse.builder()
                .id(entity.getId())
                .tabCode(entity.getTabCode())
                .tabName(entity.getTabName())
                .groupName(entity.getGroupName())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .trialCount(0L) // TODO: TrialListRepository에서 조회
                .build();
    }

    private TrialKeywordGroupResponseDTO.TabResponse toTabResponse(TrialKeywordGroup entity) {
        return TrialKeywordGroupResponseDTO.TabResponse.builder()
                .id(entity.getId())
                .tabCode(entity.getTabCode())
                .tabName(entity.getTabName())
                .displayOrder(entity.getDisplayOrder())
                .build();
    }
}