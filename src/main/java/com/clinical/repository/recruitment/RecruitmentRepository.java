package com.clinical.repository.recruitment;

import com.clinical.entity.recruitment.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    List<Recruitment> findByIsTempOrderByCreatedAtDesc(Boolean isTemp);

    List<Recruitment> findByIsTemp(Boolean isTemp);

    List<Recruitment> findAllByOrderByCreatedAtDesc();

    List<Recruitment> findByStatusAndIsTempOrderByCreatedAtDesc(String status, Boolean isTemp);

    Optional<Recruitment> findByTrialCode(String trialCode);

    boolean existsByTrialCode(String trialCode);

    List<Recruitment> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Recruitment> findByUserIdAndIsTempOrderByCreatedAtDesc(Long userId, Boolean isTemp);

    Optional<Recruitment> findByIdAndIsTemp(Long id, Boolean isTemp);

    List<Recruitment> findByStartDateBetweenOrderByCreatedAtDesc(LocalDate startDate, LocalDate endDate);

    long countByStatusAndIsTemp(String status, Boolean isTemp);

    long countByIsNotificationAndIsTemp(Boolean isNotification, Boolean isTemp);

    List<Recruitment> findByIsTempAndRecruitmentFieldIdsContaining(Boolean isTemp, String fieldId);

    List<Recruitment> findAllByOrderByUpdatedAtDesc();

    List<Recruitment> findByIsTempOrderByUpdatedAtDesc(Boolean isTemp);

    Page<Recruitment> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    Page<Recruitment> findByTitleContainingOrTrialCodeContainingOrderByUpdatedAtDesc(
            String title, String trialCode, Pageable pageable);

    Page<Recruitment> findByIsTempOrderByUpdatedAtDesc(Boolean isTemp, Pageable pageable);
}