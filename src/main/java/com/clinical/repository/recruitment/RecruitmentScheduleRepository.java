package com.clinical.repository.recruitment;

import com.clinical.entity.recruitment.RecruitmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentScheduleRepository extends JpaRepository<RecruitmentSchedule, Long> {

    List<RecruitmentSchedule> findByRecruitmentIdOrderByScheduleDateAsc(Long recruitmentId);

    Optional<RecruitmentSchedule> findByRecruitmentIdAndScheduleDate(Long recruitmentId, LocalDate scheduleDate);

    void deleteByRecruitmentId(Long recruitmentId);
}