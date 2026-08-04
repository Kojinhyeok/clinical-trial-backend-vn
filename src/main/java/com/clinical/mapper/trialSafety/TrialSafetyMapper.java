package com.clinical.mapper.trialSafety;

import com.clinical.dto.trialSafety.TrialSafetyRequestDTO;
import com.clinical.dto.trialSafety.TrialSafetyResponseDTO;
import com.clinical.entity.trial.TrialSafety;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrialSafetyMapper {

    /**
     * RequestDTO -> Entity (신규 등록용)
     */
    public TrialSafety toEntity(TrialSafetyRequestDTO dto) {
        if (dto == null) return null;

        TrialSafety entity = new TrialSafety();
        entity.setTitle(dto.getTitle());
        entity.setTrialPeriod(dto.getTrialPeriod());
        entity.setParticipantCount(dto.getParticipantCount());
        entity.setTrialLocation(dto.getTrialLocation());
        entity.setTrialOutput(dto.getTrialOutput());
        return entity;
    }

    /**
     * Entity -> ResponseDTO (조회 응답용)
     */
    public TrialSafetyResponseDTO toResponseDTO(TrialSafety entity) {
        if (entity == null) return null;

        TrialSafetyResponseDTO dto = new TrialSafetyResponseDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setTrialPeriod(entity.getTrialPeriod());
        dto.setParticipantCount(entity.getParticipantCount());
        dto.setTrialLocation(entity.getTrialLocation());
        dto.setTrialOutput(entity.getTrialOutput());

        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        return dto;
    }
}