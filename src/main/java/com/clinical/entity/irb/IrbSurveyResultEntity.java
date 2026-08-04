package com.clinical.entity.irb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "irb_survey_result")
@Getter
@Setter
public class IrbSurveyResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long irbTestId;
    private Long surveyTemplateId;
    private String reviewResult; // APPROVED, REJECTED 등
    private String reviewText;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
