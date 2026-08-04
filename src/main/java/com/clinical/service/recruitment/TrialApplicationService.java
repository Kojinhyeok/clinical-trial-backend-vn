package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.TrialApplicationRequestDTO;
import com.clinical.dto.recruitment.TrialApplicationResponseDTO;

import java.util.List;

public interface TrialApplicationService {

    TrialApplicationResponseDTO createApplication(TrialApplicationRequestDTO requestDTO);

    TrialApplicationResponseDTO updateApplication(Long id, TrialApplicationRequestDTO requestDTO);

    void deleteApplication(Long id);

    TrialApplicationResponseDTO updateStatus(Long id, String status);

    TrialApplicationResponseDTO getApplicationById(Long id);

    List<TrialApplicationResponseDTO> getAllApplications();

    List<TrialApplicationResponseDTO> getApplicationsByRecruitmentId(Long recruitmentId);

    List<TrialApplicationResponseDTO> getApplicationsByPhone(String phone);

    boolean isDuplicateApplication(Long recruitmentId, String phone);
}