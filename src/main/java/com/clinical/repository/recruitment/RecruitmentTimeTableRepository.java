package com.clinical.repository.recruitment;

import com.clinical.entity.recruitment.RecruitmentTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentTimeTableRepository extends JpaRepository<RecruitmentTimeTable, Long> {

    List<RecruitmentTimeTable> findByRecruitmentIdOrderByTimeSlotAsc(Long recruitmentId);
    Optional<RecruitmentTimeTable> findByRecruitmentIdAndTimeSlot(Long recruitmentId, String timeSlot);
    void deleteByRecruitmentId(Long recruitmentId);

    List<RecruitmentTimeTable> findByRecruitmentIdAndDateOrderByTimeSlotAsc(Long recruitmentId, LocalDate date);
    Optional<RecruitmentTimeTable> findByRecruitmentIdAndDateAndTimeSlot(Long recruitmentId, LocalDate date, String timeSlot);
    void deleteByRecruitmentIdAndDate(Long recruitmentId, LocalDate date);
}