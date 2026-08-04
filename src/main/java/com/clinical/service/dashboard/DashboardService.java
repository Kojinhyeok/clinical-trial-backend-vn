package com.clinical.service.dashboard;

import com.clinical.dto.dashboard.DashboardResponseDTO;
import com.clinical.dto.file.FileResponseDTO;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.irb.IrbCategoryEntity;
import com.clinical.entity.irb.IrbTestEntity;
import com.clinical.entity.popup.PopupEntity;
import com.clinical.entity.recruitment.Recruitment;
import com.clinical.entity.recruitment.TrialApplication;
import com.clinical.entity.statistics.CumulativeDataEntity;
import com.clinical.entity.trialRequest.TrialQuestion;
import com.clinical.repository.irb.IrbCategoryRepository;
import com.clinical.repository.irb.IrbTestRepository;
import com.clinical.repository.popup.PopupRepository;
import com.clinical.repository.recruitment.RecruitmentRepository;
import com.clinical.repository.recruitment.TrialApplicationRepository;
import com.clinical.repository.statistics.CumulativeDataRepository;
import com.clinical.repository.trialRequest.TrialQuestionRepository;
import com.clinical.repository.user.UserRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final IrbTestRepository irbTestRepository;
    private final IrbCategoryRepository irbCategoryRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final TrialApplicationRepository trialApplicationRepository;
    private final TrialQuestionRepository trialQuestionRepository;
    private final PopupRepository popupRepository;
    private final CumulativeDataRepository cumulativeDataRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData() {
        return DashboardResponseDTO.builder()
                .totalMembers(userRepository.count())
                .irbUnderReview(irbTestRepository.countByStatusAndIsTemp("IN_REVIEW", 0))
                .activeRecruitments(
                        recruitmentRepository.countByStatusAndIsTemp("OPEN", false)
                        + recruitmentRepository.countByStatusAndIsTemp("PERMANENTLY_OPEN", false)
                )
                .permanentlyOpenRecruitments(
                        recruitmentRepository.countByStatusAndIsTemp("PERMANENTLY_OPEN", false)
                )
                .unansweredInquiries(trialQuestionRepository.countByStatus("PENDING"))
                .cumulativeData(buildCumulativeData())
                .recentIrbs(buildRecentIrbs())
                .recentApplications(buildRecentApplications())
                .recentInquiries(buildRecentInquiries())
                .activePopups(buildActivePopups())
                .build();
    }

    private List<DashboardResponseDTO.CumulativeItem> buildCumulativeData() {
        return cumulativeDataRepository.findAllByOrderByIdAsc().stream()
                .map(entity -> {
                    String imageUrl = getCumulativeImageUrl(entity.getId());
                    return DashboardResponseDTO.CumulativeItem.builder()
                            .id(entity.getId())
                            .dataType(entity.getDataType())
                            .dataCount(entity.getDataCount())
                            .imageUrl(imageUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<DashboardResponseDTO.RecentIrbItem> buildRecentIrbs() {
        // 카테고리 맵 (id -> name)
        Map<Long, String> categoryMap = irbCategoryRepository.findAll().stream()
                .collect(Collectors.toMap(IrbCategoryEntity::getId, IrbCategoryEntity::getCategory));

        List<IrbTestEntity> recentIrbs = irbTestRepository
                .findTop5ByIsTempAndDepthOrderByCreatedAtDesc(0, 0);

        return recentIrbs.stream()
                .map(irb -> DashboardResponseDTO.RecentIrbItem.builder()
                        .id(irb.getId())
                        .title(irb.getTitle())
                        .categoryName(irb.getCategoryId() != null
                                ? categoryMap.getOrDefault(irb.getCategoryId(), "-")
                                : "-")
                        .status(irb.getStatus())
                        .createdAt(irb.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 최근 피험자 신청 5개 조회 (최적화 버전)
     * - 수정 전: 전체 조회 + N+1 쿼리 (100개 데이터 시 101번 쿼리)
     * - 수정 후: JOIN 쿼리 1번 (5개만 조회)
     */
    private List<DashboardResponseDTO.RecentApplicationItem> buildRecentApplications() {
        List<Object[]> results = trialApplicationRepository.findTop5WithRecruitmentForDashboard();
        
        return results.stream()
                .map(row -> {
                    Long id = ((Number) row[0]).longValue();
                    String name = (String) row[1];
                    String trialName = (String) row[2];
                    String status = (String) row[3];
                    LocalDateTime createdAt;
                    if (row[4] instanceof java.sql.Timestamp) {
                        createdAt = ((java.sql.Timestamp) row[4]).toLocalDateTime();
                    } else {
                        createdAt = (LocalDateTime) row[4];
                    }
                    
                    String maskedName = maskName(name);

                    return DashboardResponseDTO.RecentApplicationItem.builder()
                            .id(id)
                            .applicantName(maskedName)
                            .trialName(trialName)
                            .status(status)
                            .createdAt(createdAt)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 최근 시험문의 5개 조회 (최적화 버전)
     * - 수정 전: 전체 조회 후 5개만 사용 (1000개 데이터 시 1000개 조회)
     * - 수정 후: LIMIT 5로 필요한 만큼만 조회
     */
    private List<DashboardResponseDTO.RecentInquiryItem> buildRecentInquiries() {
        List<TrialQuestion> recent = trialQuestionRepository.findTop5ByOrderByCreatedAtDesc();

        return recent.stream()
                .map(q -> DashboardResponseDTO.RecentInquiryItem.builder()
                        .id(q.getId())
                        .title(q.getTitle())
                        .status(q.getStatus())
                        .createdAt(q.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private List<DashboardResponseDTO.PopupItem> buildActivePopups() {
        return popupRepository.findByIsActiveTrueOrderByPopupOrderAsc().stream()
                .map(popup -> DashboardResponseDTO.PopupItem.builder()
                        .id(popup.getId())
                        .title(popup.getTitle())
                        .isActive(popup.getIsActive())
                        .popupOrder(popup.getPopupOrder())
                        .build())
                .collect(Collectors.toList());
    }

    private String maskName(String name) {
        if (name == null || name.isEmpty()) return "-";
        if (name.length() <= 1) return name + "**";
        return name.charAt(0) + "**";
    }

    private String getCumulativeImageUrl(Long entityId) {
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntityAndCategory(
                    EntityType.CUMULATIVE_DATA, entityId, EntityCategory.THUMBNAIL
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