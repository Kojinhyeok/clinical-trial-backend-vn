package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbSurveyAnswerDTO;
import com.clinical.entity.irb.IrbSurveyAnswerEntity;
import org.springframework.stereotype.Component;

@Component
public class IrbSurveyAnswerMapper {

    public IrbSurveyAnswerDTO toDto(IrbSurveyAnswerEntity entity) {
        if (entity == null) return null;
        return IrbSurveyAnswerDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .irbTestId(entity.getIrbTestId())
                .surveyTemplateId(entity.getSurveyTemplateId())
                .answerList(entity.getAnswerList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public IrbSurveyAnswerEntity toEntity(IrbSurveyAnswerDTO dto) {
        if (dto == null) return null;
        return IrbSurveyAnswerEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .irbTestId(dto.getIrbTestId())
                .surveyTemplateId(dto.getSurveyTemplateId())
                .answerList(dto.getAnswerList())
                .build();
    }
}