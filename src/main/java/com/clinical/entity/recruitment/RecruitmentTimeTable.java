package com.clinical.entity.recruitment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_time_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recruitment_id")
    private Long recruitmentId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time_slot", length = 20)
    private String timeSlot;

    @Column(name = "max_cnt")
    private Integer maxCnt;

    @Column(name = "count")
    private Integer count;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (count == null) {
            count = 0;
        }
        if (maxCnt == null) {
            maxCnt = 0;
        }
    }
}