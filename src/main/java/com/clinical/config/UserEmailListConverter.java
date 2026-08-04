package com.clinical.config;

import com.clinical.dto.irb.UserEmailDTO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.util.List;

@Converter
public class UserEmailListConverter implements AttributeConverter<List<UserEmailDTO>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<UserEmailDTO> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<UserEmailDTO> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, UserEmailDTO.class);
            return objectMapper.readValue(dbData, listType);
        } catch (Exception e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}