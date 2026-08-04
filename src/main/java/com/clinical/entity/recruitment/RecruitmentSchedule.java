package com.clinical.entity.recruitment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recruitment_id", nullable = false)
    private Long recruitmentId;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "daily_max_cnt")
    private Integer dailyMaxCnt;

    @Column(name = "daily_count")
    private Integer dailyCount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dailyMaxCnt == null) dailyMaxCnt = 0;
        if (dailyCount == null) dailyCount = 0;
    }
}