package com.clinical.service.trial;

import com.clinical.dto.trial.TrialSearchKeywordRequestDTO;
import com.clinical.dto.trial.TrialSearchKeywordResponseDTO;
import com.clinical.entity.trial.TrialKeywordGroup;
import com.clinical.entity.trial.TrialSearchKeyword;
import com.clinical.repository.trial.TrialKeywordGroupRepository;
import com.clinical.repository.trial.TrialSearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 시험 검색 키워드 Service 구현체
 */
@Service
@RequiredArgsConstructor
public class TrialSearchKeywordServiceImpl implements TrialSearchKeywordService {

    private final TrialSearchKeywordRepository searchKeywordRepository;
    private final TrialKeywordGroupRepository keywordGroupRepository;

    @Override
    @Transactional
    public TrialSearchKeywordResponseDTO.DetailResponse createKeyword(TrialSearchKeywordRequestDTO.Create request) {
        TrialSearchKeyword keyword = TrialSearchKeyword.builder()
                .keywordGroupId(request.getKeywordGroupId())
                .keyword(request.getKeyword())
                .build();

        TrialSearchKeyword saved = searchKeywordRepository.save(keyword);
        return toDetailResponse(saved);
    }

    @Override
    @Transactional
    public List<TrialSearchKeywordResponseDTO.SimpleResponse> bulkCreateKeywords(TrialSearchKeywordRequestDTO.BulkCreate request) {
        List<TrialSearchKeyword> keywords = request.getKeywords().stream()
                .map(keyword -> TrialSearchKeyword.builder()
                        .keywordGroupId(request.getKeywordGroupId())
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());

        List<TrialSearchKeyword> saved = searchKeywordRepository.saveAll(keywords);

        return saved.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrialSearchKeywordResponseDTO.DetailResponse updateKeyword(Long id, TrialSearchKeywordRequestDTO.Update request) {
        TrialSearchKeyword keyword = searchKeywordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("키워드를 찾을 수 없습니다. ID: " + id));

        if (request.getKeyword() != null) {
            keyword.setKeyword(request.getKeyword());
        }

        TrialSearchKeyword updated = searchKeywordRepository.save(keyword);
        return toDetailResponse(updated);
    }

    @Override
    @Transactional
    public void deleteKeyword(Long id) {
        if (!searchKeywordRepository.existsById(id)) {
            throw new RuntimeException("키워드를 찾을 수 없습니다. ID: " + id);
        }
        searchKeywordRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TrialSearchKeywordResponseDTO.DetailResponse getKeywordById(Long id) {
        TrialSearchKeyword keyword = searchKeywordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("키워드를 찾을 수 없습니다. ID: " + id));
        return toDetailResponse(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialSearchKeywordResponseDTO.ListResponse> getAllKeywords() {
        return searchKeywordRepository.findAll().stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialSearchKeywordResponseDTO.ListResponse> getKeywordsByGroup(Long keywordGroupId) {
        return searchKeywordRepository.findByKeywordGroupId(keywordGroupId).stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findGroupIdsByKeyword(String keyword) {
        return searchKeywordRepository.findKeywordGroupIdsByKeyword(keyword);
    }

    // ===== Private Helper Methods =====

    private TrialSearchKeywordResponseDTO.ListResponse toListResponse(TrialSearchKeyword entity) {
        return TrialSearchKeywordResponseDTO.ListResponse.builder()
                .id(entity.getId())
                .keywordGroupId(entity.getKeywordGroupId())
                .keyword(entity.getKeyword())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private TrialSearchKeywordResponseDTO.DetailResponse toDetailResponse(TrialSearchKeyword entity) {
        // 그룹 정보 조회
        String groupName = null;
        String tabName = null;

        if (entity.getKeywordGroupId() != null) {
            TrialKeywordGroup group = keywordGroupRepository.findById(entity.getKeywordGroupId())
                    .orElse(null);
            if (group != null) {
                groupName = group.getGroupName();
                tabName = group.getTabName();
            }
        }

        return TrialSearchKeywordResponseDTO.DetailResponse.builder()
                .id(entity.getId())
                .keywordGroupId(entity.getKeywordGroupId())
                .keyword(entity.getKeyword())
                .createdAt(entity.getCreatedAt())
                .groupName(groupName)
                .tabName(tabName)
                .build();
    }

    private TrialSearchKeywordResponseDTO.SimpleResponse toSimpleResponse(TrialSearchKeyword entity) {
        return TrialSearchKeywordResponseDTO.SimpleResponse.builder()
                .id(entity.getId())
                .keyword(entity.getKeyword())
                .build();
    }
}