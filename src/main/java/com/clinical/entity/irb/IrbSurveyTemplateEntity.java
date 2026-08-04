package com.clinical.entity.irb;

import com.clinical.config.JsonConverter;
import com.clinical.dto.irb.QuestionListDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "irb_survey_template")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class IrbSurveyTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long irbTestId;
    private String surveyName; // BASIC, EXTRA

    @Convert(converter = JsonConverter.class)
    @Column(name = "question_list", columnDefinition = "json")
    private List<QuestionListDTO> questionList;

    @CreationTimestamp
    private LocalDateTime createdAt;
}