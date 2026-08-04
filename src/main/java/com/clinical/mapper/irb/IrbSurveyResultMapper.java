package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbSurveyResultDTO;
import com.clinical.entity.irb.IrbSurveyResultEntity;
import org.springframework.stereotype.Component;

@Component
public class IrbSurveyResultMapper {

    public IrbSurveyResultDTO toDto(IrbSurveyResultEntity entity) {
        if (entity == null) return null;
        return IrbSurveyResultDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .irbTestId(entity.getIrbTestId())
                .surveyTemplateId(entity.getSurveyTemplateId())
                .reviewResult(entity.getReviewResult())
                .reviewText(entity.getReviewText())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public IrbSurveyResultEntity toEntity(IrbSurveyResultDTO dto) {
        if (dto == null) return null;
        IrbSurveyResultEntity entity = new IrbSurveyResultEntity();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setIrbTestId(dto.getIrbTestId());
        entity.setSurveyTemplateId(dto.getSurveyTemplateId());
        entity.setReviewResult(dto.getReviewResult());
        entity.setReviewText(dto.getReviewText());
        return entity;
    }
}