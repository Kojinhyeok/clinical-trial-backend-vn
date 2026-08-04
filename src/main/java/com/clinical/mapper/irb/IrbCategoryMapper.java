package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbCategoryDTO;
import com.clinical.entity.irb.IrbCategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class IrbCategoryMapper {

    public IrbCategoryDTO toDto(IrbCategoryEntity entity) {
        if (entity == null) return null;
        return IrbCategoryDTO.builder()
                .id(entity.getId())
                .categoryCode(entity.getCategoryCode())
                .category(entity.getCategory())
                .build();
    }

    public IrbCategoryEntity toEntity(IrbCategoryDTO dto) {
        if (dto == null) return null;
        return IrbCategoryEntity.builder()
                .id(dto.getId())
                .categoryCode(dto.getCategoryCode())
                .category(dto.getCategory())
                .build();
    }
}