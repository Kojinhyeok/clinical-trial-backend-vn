package com.clinical.entity.irb;

import com.clinical.config.JsonConverter;
import com.clinical.config.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "irb_survey_answer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IrbSurveyAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 답변자 ID

    @Column(name = "irb_test_id")
    private Long irbTestId;

    @Column(name = "survey_template_id")
    private Long surveyTemplateId;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "answer_list", columnDefinition = "json")
    private Map<String, Object> answerList; // JSON 데이터: {"q1": true, "q2": false, "q3": "텍스트"}

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}