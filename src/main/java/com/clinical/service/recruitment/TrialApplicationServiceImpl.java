package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.TrialApplicationRequestDTO;
import com.clinical.dto.recruitment.TrialApplicationResponseDTO;
import com.clinical.entity.recruitment.TrialApplication;
import com.clinical.exception.NotFoundException;
import com.clinical.repository.recruitment.TrialApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TrialApplicationServiceImpl implements TrialApplicationService {

    private final TrialApplicationRepository applicationRepository;

    @Override
    @Transactional
    public TrialApplicationResponseDTO createApplication(TrialApplicationRequestDTO requestDTO) {
        if (isDuplicateApplication(requestDTO.getRecruitmentId(), requestDTO.getPhone())) {
            throw new IllegalStateException("이미 신청한 모집공고입니다.");
        }

        TrialApplication application = TrialApplication.builder()
                .recruitmentId(requestDTO.getRecruitmentId())
                .recruitmentFieldId(requestDTO.getRecruitmentFieldId())
                .status("PENDING")
                .startDate(requestDTO.getStartDate())
                .startTime(requestDTO.getStartTime())
                .name(requestDTO.getName())
                .gender(requestDTO.getGender())
                .birth(requestDTO.getBirth())
                .phone(requestDTO.getPhone())
                .build();

        TrialApplication saved = applicationRepository.save(application);
        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public TrialApplicationResponseDTO updateApplication(Long id, TrialApplicationRequestDTO requestDTO) {
        TrialApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("신청을 찾을 수 없습니다. ID: " + id));

        application.setStartDate(requestDTO.getStartDate());
        application.setStartTime(requestDTO.getStartTime());
        application.setName(requestDTO.getName());
        application.setGender(requestDTO.getGender());
        application.setBirth(requestDTO.getBirth());
        application.setPhone(requestDTO.getPhone());
        if (requestDTO.getRecruitmentFieldId() != null) {
            application.setRecruitmentFieldId(requestDTO.getRecruitmentFieldId());
        }

        TrialApplication updated = applicationRepository.save(application);
        return convertToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new NotFoundException("신청을 찾을 수 없습니다. ID: " + id);
        }
        applicationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TrialApplicationResponseDTO updateStatus(Long id, String status) {
        TrialApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("신청을 찾을 수 없습니다. ID: " + id));

        application.setStatus(status);
        TrialApplication updated = applicationRepository.save(application);
        return convertToResponseDTO(updated);
    }

    @Override
    public TrialApplicationResponseDTO getApplicationById(Long id) {
        TrialApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("신청을 찾을 수 없습니다. ID: " + id));
        return convertToResponseDTO(application);
    }

    @Override
    public List<TrialApplicationResponseDTO> getAllApplications() {
        List<TrialApplication> applications = applicationRepository.findAllByOrderByCreatedAtDesc();
        return applications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrialApplicationResponseDTO> getApplicationsByRecruitmentId(Long recruitmentId) {
        List<TrialApplication> applications = applicationRepository
                .findByRecruitmentIdOrderByCreatedAtDesc(recruitmentId);
        return applications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrialApplicationResponseDTO> getApplicationsByPhone(String phone) {
        List<TrialApplication> applications = applicationRepository.findByPhoneOrderByCreatedAtDesc(phone);
        return applications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDuplicateApplication(Long recruitmentId, String phone) {
        return applicationRepository.existsByRecruitmentIdAndPhone(recruitmentId, phone);
    }

    private TrialApplicationResponseDTO convertToResponseDTO(TrialApplication application) {
        return TrialApplicationResponseDTO.builder()
                .id(application.getId())
                .recruitmentId(application.getRecruitmentId())
                .recruitmentFieldId(application.getRecruitmentFieldId())
                .status(application.getStatus())
                .startDate(application.getStartDate())
                .startTime(application.getStartTime())
                .name(application.getName())
                .gender(application.getGender())
                .birth(application.getBirth())
                .phone(application.getPhone())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}