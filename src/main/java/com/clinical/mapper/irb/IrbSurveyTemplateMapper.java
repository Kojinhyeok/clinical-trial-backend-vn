package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbSurveyTemplateDTO;
import com.clinical.entity.irb.IrbSurveyTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class IrbSurveyTemplateMapper {

    public IrbSurveyTemplateDTO toDto(IrbSurveyTemplateEntity entity) {
        if (entity == null) return null;
        return IrbSurveyTemplateDTO.builder()
                .id(entity.getId())
                .irbTestId(entity.getIrbTestId())
                .surveyName(entity.getSurveyName())
                .questionList(entity.getQuestionList())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public IrbSurveyTemplateEntity toEntity(IrbSurveyTemplateDTO dto) {
        if (dto == null) return null;
        return IrbSurveyTemplateEntity.builder()
                .irbTestId(dto.getIrbTestId())
                .surveyName(dto.getSurveyName())
                .questionList(dto.getQuestionList())
                .build();
    }
}