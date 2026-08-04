package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbSurveyQuestionDTO;
import com.clinical.entity.irb.IrbSurveyQuestionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IrbSurveyQuestionMapper {

    public IrbSurveyQuestionDTO toDto(IrbSurveyQuestionEntity entity) {
        if (entity == null) return null;
        return IrbSurveyQuestionDTO.builder()
                .id(entity.getId())
                .questionCategory(entity.getQuestionCategory())
                .questionType(entity.getQuestionType())
                .questionText(entity.getQuestionText())
                .questionOrder(entity.getQuestionOrder())
                .isActive(entity.getIsActive())
                .build();
    }

    public IrbSurveyQuestionEntity toEntity(IrbSurveyQuestionDTO dto) {
        if (dto == null) return null;
        return IrbSurveyQuestionEntity.builder()
                .id(dto.getId())
                .questionCategory(dto.getQuestionCategory())
                .questionType(dto.getQuestionType())
                .questionText(dto.getQuestionText())
                .questionOrder(dto.getQuestionOrder())
                .isActive(dto.getIsActive())
                .build();
    }
}