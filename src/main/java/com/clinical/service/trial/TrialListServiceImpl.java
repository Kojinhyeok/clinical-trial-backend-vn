package com.clinical.service.trial;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.trial.TrialListRequestDTO;
import com.clinical.dto.trial.TrialListResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.trial.TrialKeywordGroup;
import com.clinical.entity.trial.TrialList;
import com.clinical.repository.trial.TrialKeywordGroupRepository;
import com.clinical.repository.trial.TrialListRepository;
import com.clinical.repository.trial.TrialSearchKeywordRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TrialList Service 구현체
 *
 * 유효성 평가 페이지 플로우:
 * 1. 탭 선택 (BASIC, MAKEUP 등)
 * 2. KeywordGroup으로 그룹화된 카드 표시
 * 3. 검색어 입력 시 현재 탭 내에서 필터링
 * 4. 조회수 기능 없음 (단순 정보 표시용)
 */
@Service
@RequiredArgsConstructor
public class TrialListServiceImpl implements TrialListService {

    private final TrialListRepository trialListRepository;
    private final TrialKeywordGroupRepository keywordGroupRepository;
    private final TrialSearchKeywordRepository searchKeywordRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public TrialListResponseDTO.DetailResponse createTrial(TrialListRequestDTO.Create request) {
        TrialList trial = TrialList.builder()
                .keywordGroupId(request.getKeywordGroupId())
                .trialTitle(request.getTrialTitle())
                .trialSubtitle(request.getTrialSubtitle())
                .trialDescription(request.getTrialDescription())
                .trialPersonnel(request.getTrialPersonnel())
                .trialRequiredSample(request.getTrialRequiredSample())
                .trialReportDate(request.getTrialReportDate())
                .trialReportDateSub(request.getTrialReportDateSub())
                .trialTimePoint(request.getTrialTimePoint())
                .trialPart(request.getTrialPart())
                .trialResultType(request.getTrialResultType())
                .build();

        TrialList saved = trialListRepository.save(trial);
        return toDetailResponse(saved);
    }

    @Override
    @Transactional
    public TrialListResponseDTO.DetailResponse updateTrial(Long id, TrialListRequestDTO.Update request) {
        TrialList trial = trialListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시험을 찾을 수 없습니다: " + id));

        if (request.getKeywordGroupId() != null) trial.setKeywordGroupId(request.getKeywordGroupId());
        trial.setTrialTitle(request.getTrialTitle());
        trial.setTrialSubtitle(request.getTrialSubtitle());
        trial.setTrialDescription(request.getTrialDescription());
        trial.setTrialPersonnel(request.getTrialPersonnel());
        trial.setTrialRequiredSample(request.getTrialRequiredSample());
        trial.setTrialReportDate(request.getTrialReportDate());
        trial.setTrialReportDateSub(request.getTrialReportDateSub());
        trial.setTrialTimePoint(request.getTrialTimePoint());
        trial.setTrialPart(request.getTrialPart());
        trial.setTrialResultType(request.getTrialResultType());

        TrialList updated = trialListRepository.save(trial);
        return toDetailResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTrial(Long id) {
        if (!trialListRepository.existsById(id)) {
            throw new RuntimeException("시험을 찾을 수 없습니다: " + id);
        }
        trialListRepository.deleteById(id);
    }

    /**
     * 시험 상세 조회
     * ⚠️ 조회수 증가 로직 없음 (유효성 평가 페이지는 조회수 기능 없음)
     */
    @Override
    @Transactional(readOnly = true)
    public TrialListResponseDTO.DetailResponse getTrialById(Long id) {
        TrialList trial = trialListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시험을 찾을 수 없습니다: " + id));
        return toDetailResponse(trial);
    }

    /**
     * 전체 목록 조회 - 최적화 버전
     * 파일을 trial별로 개별 조회하지 않고 한 번에 가져와서 메모리에서 매핑
     * 기존: 100 trials × 4 쿼리 = 400번 → 개선: trials 1번 + files 1번 + keywordGroup ~12번 = ~14번
     */
    @Override
    @Transactional(readOnly = true)
    public List<TrialListResponseDTO.ListResponse> getAllTrials() {
        List<TrialList> trials = trialListRepository.findAllByOrderByCreatedAtDesc();
        if (trials.isEmpty()) return List.of();

        // 전체 trial ID로 파일을 한 번에 조회
        List<Long> trialIds = trials.stream()
                .map(TrialList::getId)
                .collect(Collectors.toList());

        List<FileResponseDTO> allFiles = fileService
                .getFilesByEntityTypeAndEntityIds(EntityType.TRIAL_LIST, trialIds);

        // entityId 기준으로 그룹핑
        Map<Long, List<FileResponseDTO>> fileMap = allFiles.stream()
                .collect(Collectors.groupingBy(FileResponseDTO::getEntityId));

        return trials.stream()
                .map(trial -> toListResponseWithFiles(
                        trial,
                        fileMap.getOrDefault(trial.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrialListResponseDTO.ListResponse> getTrialsByGroup(Long keywordGroupId) {
        return trialListRepository.findByKeywordGroupId(keywordGroupId).stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 키워드 검색 (탭 내에서 필터링)
     */
    @Override
    @Transactional(readOnly = true)
    public List<TrialListResponseDTO.ListResponse> searchTrialsByKeyword(String keyword, String tabCode) {
        List<Long> groupIds = searchKeywordRepository.findKeywordGroupIdsByKeyword(keyword);
        if (groupIds.isEmpty()) return List.of();

        if (tabCode != null && !tabCode.isEmpty()) {
            List<TrialKeywordGroup> tabGroups = keywordGroupRepository.findByTabCode(tabCode);
            List<Long> tabGroupIds = tabGroups.stream()
                    .map(TrialKeywordGroup::getId)
                    .collect(Collectors.toList());
            groupIds = groupIds.stream()
                    .filter(tabGroupIds::contains)
                    .collect(Collectors.toList());
            if (groupIds.isEmpty()) return List.of();
        }

        return trialListRepository.findByKeywordGroupIdIn(groupIds).stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    // ===========================
    // Helper 메서드
    // ===========================

    /**
     * Entity → ListResponse 변환 (파일 개별 조회 버전 - getTrialsByGroup, searchTrialsByKeyword용)
     */
    private TrialListResponseDTO.ListResponse toListResponse(TrialList entity) {
        String tabCode = null, groupName = null, tabName = null;
        if (entity.getKeywordGroupId() != null) {
            TrialKeywordGroup group = keywordGroupRepository.findById(entity.getKeywordGroupId()).orElse(null);
            if (group != null) {
                tabCode   = group.getTabCode();
                groupName = group.getGroupName();
                tabName   = group.getTabName();
            }
        }

        String beforeImageUrl = getImagePresignedUrl(entity.getId(), EntityCategory.BEFORE);
        String afterImageUrl  = getImagePresignedUrl(entity.getId(), EntityCategory.AFTER);
        List<TrialListResponseDTO.ImagePair> imagePairs = getImagePairs(entity.getId());

        return TrialListResponseDTO.ListResponse.builder()
                .id(entity.getId())
                .keywordGroupId(entity.getKeywordGroupId())
                .tabCode(tabCode)
                .groupName(groupName)
                .tabName(tabName)
                .trialTitle(entity.getTrialTitle())
                .trialSubtitle(entity.getTrialSubtitle())
                .trialTimePoint(entity.getTrialTimePoint())
                .trialPart(entity.getTrialPart())
                .trialResultType(entity.getTrialResultType())
                .beforeImageUrl(beforeImageUrl)
                .afterImageUrl(afterImageUrl)
                .imagePairs(imagePairs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Entity → ListResponse 변환 (파일 미리 로드된 버전 - getAllTrials() 최적화용)
     * DB 추가 조회 없이 전달받은 files 목록에서 메모리 매핑
     */
    private TrialListResponseDTO.ListResponse toListResponseWithFiles(
            TrialList entity, List<FileResponseDTO> files) {

        String tabCode = null, groupName = null, tabName = null;
        if (entity.getKeywordGroupId() != null) {
            TrialKeywordGroup group = keywordGroupRepository.findById(entity.getKeywordGroupId()).orElse(null);
            if (group != null) {
                tabCode   = group.getTabCode();
                groupName = group.getGroupName();
                tabName   = group.getTabName();
            }
        }

        // 파일 목록에서 쌍 조립 (메모리에서 처리, DB 추가 조회 없음)
        List<TrialListResponseDTO.ImagePair> imagePairs = buildImagePairsFromFiles(files);

        String beforeImageUrl = imagePairs.isEmpty() ? null : imagePairs.get(0).getBeforeImageUrl();
        String afterImageUrl  = imagePairs.isEmpty() ? null : imagePairs.get(0).getAfterImageUrl();

        return TrialListResponseDTO.ListResponse.builder()
                .id(entity.getId())
                .keywordGroupId(entity.getKeywordGroupId())
                .tabCode(tabCode)
                .groupName(groupName)
                .tabName(tabName)
                .trialTitle(entity.getTrialTitle())
                .trialSubtitle(entity.getTrialSubtitle())
                .trialTimePoint(entity.getTrialTimePoint())
                .trialPart(entity.getTrialPart())
                .trialResultType(entity.getTrialResultType())
                .beforeImageUrl(beforeImageUrl)
                .afterImageUrl(afterImageUrl)
                .imagePairs(imagePairs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * 파일 목록 → 이미지 쌍 배열 변환 (메모리에서 처리)
     */
    private List<TrialListResponseDTO.ImagePair> buildImagePairsFromFiles(List<FileResponseDTO> files) {
        // fileCategory → s3Url Map
        Map<String, String> categoryUrlMap = files.stream()
                .filter(f -> f.getFileCategory() != null && f.getS3Url() != null)
                .collect(Collectors.toMap(
                        FileResponseDTO::getFileCategory,
                        FileResponseDTO::getS3Url,
                        (a, b) -> a  // 중복 시 첫 번째 유지
                ));

        List<TrialListResponseDTO.ImagePair> pairs = new ArrayList<>();
        int i = 1;
        while (true) {
            String beforeUrl = categoryUrlMap.get("BEFORE_" + i);
            String afterUrl  = categoryUrlMap.get("AFTER_"  + i);
            if (beforeUrl == null && afterUrl == null) break;
            pairs.add(TrialListResponseDTO.ImagePair.builder()
                    .pairIndex(i)
                    .beforeImageUrl(beforeUrl)
                    .afterImageUrl(afterUrl)
                    .build());
            i++;
        }

        // 구버전 BEFORE/AFTER 폴백
        if (pairs.isEmpty()) {
            String beforeUrl = categoryUrlMap.get("BEFORE");
            String afterUrl  = categoryUrlMap.get("AFTER");
            if (beforeUrl != null || afterUrl != null) {
                pairs.add(TrialListResponseDTO.ImagePair.builder()
                        .pairIndex(1)
                        .beforeImageUrl(beforeUrl)
                        .afterImageUrl(afterUrl)
                        .build());
            }
        }
        return pairs;
    }

    /**
     * Entity → DetailResponse 변환
     */
    private TrialListResponseDTO.DetailResponse toDetailResponse(TrialList entity) {
        String tabCode = null, groupName = null, tabName = null;
        if (entity.getKeywordGroupId() != null) {
            TrialKeywordGroup group = keywordGroupRepository.findById(entity.getKeywordGroupId()).orElse(null);
            if (group != null) {
                tabCode   = group.getTabCode();
                groupName = group.getGroupName();
                tabName   = group.getTabName();
            }
        }

        String beforeImageUrl = getImagePresignedUrl(entity.getId(), EntityCategory.BEFORE);
        String afterImageUrl  = getImagePresignedUrl(entity.getId(), EntityCategory.AFTER);

        return TrialListResponseDTO.DetailResponse.builder()
                .id(entity.getId())
                .keywordGroupId(entity.getKeywordGroupId())
                .tabCode(tabCode)
                .groupName(groupName)
                .tabName(tabName)
                .trialTitle(entity.getTrialTitle())
                .trialSubtitle(entity.getTrialSubtitle())
                .trialDescription(entity.getTrialDescription())
                .trialPersonnel(entity.getTrialPersonnel())
                .trialRequiredSample(entity.getTrialRequiredSample())
                .trialReportDate(entity.getTrialReportDate())
                .trialReportDateSub(entity.getTrialReportDateSub())
                .trialTimePoint(entity.getTrialTimePoint())
                .trialPart(entity.getTrialPart())
                .trialResultType(entity.getTrialResultType())
                .beforeImageUrl(beforeImageUrl)
                .afterImageUrl(afterImageUrl)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * 이미지 쌍 목록 조회 (개별 조회용)
     */
    private List<TrialListResponseDTO.ImagePair> getImagePairs(Long trialId) {
        List<TrialListResponseDTO.ImagePair> pairs = new ArrayList<>();
        int pairIndex = 1;
        while (true) {
            String beforeUrl = getImageUrlByCategory(trialId, "BEFORE_" + pairIndex);
            String afterUrl  = getImageUrlByCategory(trialId, "AFTER_"  + pairIndex);
            if (beforeUrl == null && afterUrl == null) break;
            pairs.add(TrialListResponseDTO.ImagePair.builder()
                    .pairIndex(pairIndex)
                    .beforeImageUrl(beforeUrl)
                    .afterImageUrl(afterUrl)
                    .build());
            pairIndex++;
        }

        if (pairs.isEmpty()) {
            String beforeUrl = getImageUrlByCategory(trialId, "BEFORE");
            String afterUrl  = getImageUrlByCategory(trialId, "AFTER");
            if (beforeUrl != null || afterUrl != null) {
                pairs.add(TrialListResponseDTO.ImagePair.builder()
                        .pairIndex(1)
                        .beforeImageUrl(beforeUrl)
                        .afterImageUrl(afterUrl)
                        .build());
            }
        }
        return pairs;
    }

    /**
     * String category로 단일 URL 조회
     */
    private String getImageUrlByCategory(Long trialId, String category) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.TRIAL_LIST, trialId, category);
            if (files == null || files.isEmpty()) return null;
            return files.get(0).getS3Url();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 이미지 Presigned URL 조회 (BEFORE_1 우선, 없으면 구버전 폴백)
     */
    private String getImagePresignedUrl(Long trialId, EntityCategory category) {
        String url = getImageUrlByCategory(trialId, category.name() + "_1");
        if (url == null) {
            url = getImageUrlByCategory(trialId, category.name());
        }
        return url;
    }
}