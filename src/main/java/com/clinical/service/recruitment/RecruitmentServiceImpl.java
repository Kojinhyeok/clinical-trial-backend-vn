package com.clinical.service.recruitment;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.recruitment.RecruitmentRequestDTO;
import com.clinical.dto.recruitment.RecruitmentResponseDTO;
import com.clinical.dto.recruitment.RecruitmentSummaryDTO;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.recruitment.Recruitment;
import com.clinical.entity.recruitment.RecruitmentField;
import com.clinical.exception.NotFoundException;
import com.clinical.repository.recruitment.RecruitmentRepository;
import com.clinical.repository.recruitment.RecruitmentFieldRepository;
import com.clinical.repository.recruitment.RecruitmentScheduleRepository;
import com.clinical.repository.recruitment.TrialApplicationRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecruitmentServiceImpl implements RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentFieldRepository recruitmentFieldRepository;
    private final TrialApplicationRepository trialApplicationRepository;
    private final RecruitmentScheduleRepository scheduleRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public RecruitmentResponseDTO createRecruitment(RecruitmentRequestDTO requestDTO) {
        Recruitment recruitment = Recruitment.builder()
                .userId(requestDTO.getUserId())
                .isNotification(requestDTO.getIsNotification())
                .isTemp(requestDTO.getIsTemp())
                .recruitmentFieldIds(requestDTO.getRecruitmentFieldIds())
                .trialCode(requestDTO.getTrialCode())
                .trialName(requestDTO.getTrialName())
                .participationNumber(requestDTO.getParticipationNumber())
                .participationGroup(requestDTO.getParticipationGroup())
                .trialPart(requestDTO.getTrialPart())
                .participationCost(requestDTO.getParticipationCost())
                .requirements(requestDTO.getRequirements())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .status(requestDTO.getStatus())
                .build();

        Recruitment saved = recruitmentRepository.save(recruitment);
        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public RecruitmentResponseDTO updateRecruitment(Long id, RecruitmentRequestDTO requestDTO) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("모집공고를 찾을 수 없습니다. ID: " + id));

        recruitment.setUserId(requestDTO.getUserId());
        recruitment.setIsNotification(requestDTO.getIsNotification());
        recruitment.setIsTemp(requestDTO.getIsTemp());
        recruitment.setRecruitmentFieldIds(requestDTO.getRecruitmentFieldIds());
        recruitment.setTrialCode(requestDTO.getTrialCode());
        recruitment.setTrialName(requestDTO.getTrialName());
        recruitment.setParticipationNumber(requestDTO.getParticipationNumber());
        recruitment.setParticipationGroup(requestDTO.getParticipationGroup());
        recruitment.setTrialPart(requestDTO.getTrialPart());
        recruitment.setParticipationCost(requestDTO.getParticipationCost());
        recruitment.setRequirements(requestDTO.getRequirements());
        recruitment.setStartDate(requestDTO.getStartDate());
        recruitment.setEndDate(requestDTO.getEndDate());
        recruitment.setTitle(requestDTO.getTitle());
        recruitment.setContent(requestDTO.getContent());
        recruitment.setStatus(requestDTO.getStatus());

        Recruitment updated = recruitmentRepository.save(recruitment);
        return convertToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteRecruitment(Long id) {
        if (!recruitmentRepository.existsById(id)) {
            throw new NotFoundException("모집공고를 찾을 수 없습니다. ID: " + id);
        }
        trialApplicationRepository.deleteByRecruitmentId(id);
        scheduleRepository.deleteByRecruitmentId(id);
        try {
            fileService.deleteAllByEntity(EntityType.RECRUITMENT, id);
        } catch (Exception e) {
            log.warn("모집공고 첨부파일 삭제 실패: {}", e.getMessage());
        }
        recruitmentRepository.deleteById(id);
    }

    @Override
    public RecruitmentResponseDTO getRecruitmentById(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("모집공고를 찾을 수 없습니다. ID: " + id));
        RecruitmentResponseDTO dto = convertToResponseDTO(recruitment);
        try {
            List<FileResponseDTO> files = fileService.getFilesByEntity(EntityType.RECRUITMENT, id);
            dto.setAttachedFiles(files);
        } catch (Exception e) {
            log.warn("모집공고 첨부파일 조회 실패: {}", e.getMessage());
        }
        return dto;
    }

    @Override
    public List<RecruitmentResponseDTO> getAllRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findByIsTempOrderByUpdatedAtDesc(false);
        return recruitments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentResponseDTO> getAllRecruitmentsIncludingTemp() {
        List<Recruitment> recruitments = recruitmentRepository.findAllByOrderByUpdatedAtDesc();
        return recruitments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentSummaryDTO> getRecruitmentsByField(String fieldCode) {
        RecruitmentField field = recruitmentFieldRepository.findByFieldCode(fieldCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지역 코드입니다: " + fieldCode));

        Long fieldId = field.getId();
        String fieldIdStr = String.valueOf(fieldId);

        List<Recruitment> filtered = recruitmentRepository
                .findByIsTempAndRecruitmentFieldIdsContaining(false, fieldIdStr);

        return filtered.stream()
                .sorted((r1, r2) -> {
                    if (Boolean.TRUE.equals(r1.getIsNotification()) && !Boolean.TRUE.equals(r2.getIsNotification())) return -1;
                    if (!Boolean.TRUE.equals(r1.getIsNotification()) && Boolean.TRUE.equals(r2.getIsNotification())) return 1;
                    return r2.getUpdatedAt().compareTo(r1.getUpdatedAt());
                })
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentResponseDTO> getRecruitmentsByDate(LocalDate date) {
        List<Recruitment> allRecruitments = recruitmentRepository.findByIsTempOrderByCreatedAtDesc(false);

        return allRecruitments.stream()
                .filter(recruitment -> {
                    if (Boolean.TRUE.equals(recruitment.getIsNotification())) {
                        return false;
                    }

                    if (recruitment.getStartDate() == null || recruitment.getEndDate() == null) {
                        return false;
                    }
                    return !date.isBefore(recruitment.getStartDate()) && !date.isAfter(recruitment.getEndDate());
                })
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RecruitmentResponseDTO> createRecruitmentBulk(RecruitmentRequestDTO requestDTO) {
        List<Long> fieldIds = parseFieldIds(requestDTO.getRecruitmentFieldIds());

        if (fieldIds.isEmpty()) {
            throw new IllegalArgumentException("지역을 1개 이상 선택해주세요.");
        }

        if (fieldIds.size() == 1) {
            return List.of(createRecruitment(requestDTO));
        }

        List<RecruitmentResponseDTO> results = new ArrayList<>();
        for (Long fieldId : fieldIds) {
            Recruitment recruitment = Recruitment.builder()
                    .userId(requestDTO.getUserId())
                    .isNotification(requestDTO.getIsNotification())
                    .isTemp(requestDTO.getIsTemp())
                    .recruitmentFieldIds("[" + fieldId + "]")
                    .trialCode(requestDTO.getTrialCode())
                    .trialName(requestDTO.getTrialName())
                    .participationNumber(requestDTO.getParticipationNumber())
                    .participationGroup(requestDTO.getParticipationGroup())
                    .trialPart(requestDTO.getTrialPart())
                    .participationCost(requestDTO.getParticipationCost())
                    .requirements(requestDTO.getRequirements())
                    .startDate(requestDTO.getStartDate())
                    .endDate(requestDTO.getEndDate())
                    .title(requestDTO.getTitle())
                    .content(requestDTO.getContent())
                    .status(requestDTO.getStatus())
                    .build();

            Recruitment saved = recruitmentRepository.save(recruitment);
            results.add(convertToResponseDTO(saved));
        }

        log.info("모집공고 일괄 생성 완료: {}건", results.size());
        return results;
    }

    private List<Long> parseFieldIds(String jsonStr) {
        if (jsonStr == null || jsonStr.isBlank()) {
            return List.of();
        }
        String stripped = jsonStr.replaceAll("[\\[\\]\\s]", "");
        if (stripped.isEmpty()) return List.of();
        return Arrays.stream(stripped.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private RecruitmentResponseDTO convertToResponseDTO(Recruitment recruitment) {
        return RecruitmentResponseDTO.builder()
                .id(recruitment.getId())
                .userId(recruitment.getUserId())
                .isNotification(recruitment.getIsNotification())
                .isTemp(recruitment.getIsTemp())
                .recruitmentFieldIds(recruitment.getRecruitmentFieldIds())
                .trialCode(recruitment.getTrialCode())
                .trialName(recruitment.getTrialName())
                .participationNumber(recruitment.getParticipationNumber())
                .participationGroup(recruitment.getParticipationGroup())
                .trialPart(recruitment.getTrialPart())
                .participationCost(recruitment.getParticipationCost())
                .requirements(recruitment.getRequirements())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .status(recruitment.getStatus())
                .createdAt(recruitment.getCreatedAt())
                .updatedAt(recruitment.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void touch(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("모집공고를 찾을 수 없습니다. ID: " + id));
        recruitment.setUpdatedAt(java.time.LocalDateTime.now());
        recruitmentRepository.save(recruitment);
    }

    private RecruitmentSummaryDTO convertToSummaryDTO(Recruitment recruitment) {
        return RecruitmentSummaryDTO.builder()
                .id(recruitment.getId())
                .userId(recruitment.getUserId())
                .isNotification(recruitment.getIsNotification())
                .isTemp(recruitment.getIsTemp())
                .recruitmentFieldIds(recruitment.getRecruitmentFieldIds())
                .trialCode(recruitment.getTrialCode())
                .trialName(recruitment.getTrialName())
                .participationNumber(recruitment.getParticipationNumber())
                .participationGroup(recruitment.getParticipationGroup())
                .trialPart(recruitment.getTrialPart())
                .participationCost(recruitment.getParticipationCost())
                .requirements(recruitment.getRequirements())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .title(recruitment.getTitle())
                .status(recruitment.getStatus())
                .createdAt(recruitment.getCreatedAt())
                .updatedAt(recruitment.getUpdatedAt())
                .build();
    }

    @Override
    public Page<RecruitmentSummaryDTO> getRecruitmentsForAdmin(String keyword, String status, Pageable pageable) {
        Page<Recruitment> page;

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasKeyword) {
            page = recruitmentRepository
                    .findByTitleContainingOrTrialCodeContainingOrderByUpdatedAtDesc(
                            keyword, keyword, pageable);
        } else if (hasStatus && status.equals("TEMP")) {
            page = recruitmentRepository.findByIsTempOrderByUpdatedAtDesc(true, pageable);
        } else if (hasStatus) {
            page = recruitmentRepository.findAllByOrderByUpdatedAtDesc(pageable);
            List<Recruitment> filtered = page.getContent().stream()
                    .filter(r -> !Boolean.TRUE.equals(r.getIsTemp()))
                    .filter(r -> status.equals(r.getStatus()))
                    .collect(Collectors.toList());
            page = new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            page = recruitmentRepository.findAllByOrderByUpdatedAtDesc(pageable);
        }

        return page.map(this::convertToSummaryDTO);
    }
}