package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.RecruitmentRequestDTO;
import com.clinical.dto.recruitment.RecruitmentResponseDTO;
import com.clinical.dto.recruitment.RecruitmentSummaryDTO;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentService {

    RecruitmentResponseDTO createRecruitment(RecruitmentRequestDTO requestDTO);

    RecruitmentResponseDTO updateRecruitment(Long id, RecruitmentRequestDTO requestDTO);

    void deleteRecruitment(Long id);

    RecruitmentResponseDTO getRecruitmentById(Long id);

    List<RecruitmentResponseDTO> getAllRecruitments();

    List<RecruitmentResponseDTO> getAllRecruitmentsIncludingTemp();

    List<RecruitmentSummaryDTO> getRecruitmentsByField(String fieldCode);

    List<RecruitmentResponseDTO> getRecruitmentsByDate(LocalDate date);

    List<RecruitmentResponseDTO> createRecruitmentBulk(RecruitmentRequestDTO requestDTO);

    void touch(Long id);

    Page<RecruitmentSummaryDTO> getRecruitmentsForAdmin(String keyword, String status, Pageable pageable);
}