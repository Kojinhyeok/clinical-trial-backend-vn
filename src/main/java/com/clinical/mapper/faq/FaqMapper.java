package com.clinical.mapper.faq;

import com.clinical.dto.faq.FaqRequestDTO;
import com.clinical.dto.faq.FaqResponseDTO;
import com.clinical.entity.faq.FaqEntity;
import org.springframework.stereotype.Component;

import static com.clinical.entity.enumuration.FaqType.fromValue;

@Component
public class FaqMapper {
    public FaqEntity toEntity(FaqRequestDTO dto) {
        return FaqEntity.builder()
                .userId(dto.getUserId())
                .faqType(dto.getFaqType().toString())
                .title(dto.getTitle())
                .content(dto.getContent())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .build();
    }

    public FaqResponseDTO toDto(FaqEntity entity) {
        return FaqResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .faqType(fromValue(entity.getFaqType()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}