package com.clinical.repository.trialRequest;

import com.clinical.entity.trialRequest.TrialFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrialFilesRepository extends JpaRepository<TrialFiles, Long> {

    List<TrialFiles> findByIsTempOrderByCreatedAtDesc(Boolean isTemp);

    List<TrialFiles> findByIsTemp(Boolean isTemp);

    List<TrialFiles> findAllByOrderByCreatedAtDesc();

    List<TrialFiles> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TrialFiles> findByUserIdAndIsTempOrderByCreatedAtDesc(Long userId, Boolean isTemp);

    Optional<TrialFiles> findByIdAndIsTemp(Long id, Boolean isTemp);

    long countByIsNotificationAndIsTemp(Boolean isNotification, Boolean isTemp);
}