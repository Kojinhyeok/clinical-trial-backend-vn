package com.clinical.entity.recruitment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_notification")
    private Boolean isNotification;

    @Column(name = "is_temp")
    private Boolean isTemp;

    @Column(name = "recruitment_field_ids", columnDefinition = "JSON")
    private String recruitmentFieldIds;

    @Column(name = "trial_code", length = 50)
    private String trialCode;

    @Column(name = "trial_name", length = 255)
    private String trialName;

    @Column(name = "participation_number")
    private Integer participationNumber;

    @Column(name = "participation_group", length = 255)
    private String participationGroup;

    @Column(name = "trial_part", length = 255)
    private String trialPart;

    @Column(name = "participation_cost", length = 100)
    private String participationCost;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isNotification == null) {
            isNotification = false;
        }
        if (isTemp == null) {
            isTemp = false;
        }
        if (status == null) {
            status = "OPEN";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}