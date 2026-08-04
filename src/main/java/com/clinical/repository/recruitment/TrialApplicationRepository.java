package com.clinical.repository.recruitment;

import com.clinical.entity.recruitment.TrialApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrialApplicationRepository extends JpaRepository<TrialApplication, Long> {

    List<TrialApplication> findByRecruitmentIdOrderByCreatedAtDesc(Long recruitmentId);

    List<TrialApplication> findByRecruitmentIdAndStatusOrderByCreatedAtDesc(Long recruitmentId, String status);

    List<TrialApplication> findAllByOrderByCreatedAtDesc();

    List<TrialApplication> findByStatusOrderByCreatedAtDesc(String status);

    List<TrialApplication> findByPhoneOrderByCreatedAtDesc(String phone);

    List<TrialApplication> findByNameAndPhoneOrderByCreatedAtDesc(String name, String phone);

    boolean existsByRecruitmentIdAndPhone(Long recruitmentId, String phone);

    long countByRecruitmentId(Long recruitmentId);

    long countByRecruitmentIdAndStatus(Long recruitmentId, String status);

    List<TrialApplication> findByStartDateOrderByStartTimeAsc(LocalDate startDate);

    List<TrialApplication> findByRecruitmentIdAndStartDateAndStartTime(
        Long recruitmentId,
        LocalDate startDate,
        String startTime
    );

    void deleteByRecruitmentId(Long recruitmentId);

    @Query(value = """
        SELECT
            ta.id,
            ta.name,
            COALESCE(r.trial_name, '-') as trial_name,
            ta.status,
            ta.created_at
        FROM trial_application ta
        LEFT JOIN recruitment r ON ta.recruitment_id = r.id
        ORDER BY ta.created_at DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5WithRecruitmentForDashboard();
}