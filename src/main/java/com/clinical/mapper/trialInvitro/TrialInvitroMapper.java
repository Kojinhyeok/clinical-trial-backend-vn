package com.clinical.mapper.trialInvitro;

import com.clinical.dto.trialInvitro.TrialInvitroRequestDTO;
import com.clinical.dto.trialInvitro.TrialInvitroResponseDTO;
import com.clinical.entity.trial.TrialInvitro;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class TrialInvitroMapper {

    /**
     * RequestDTO -> Entity (신규 등록용)
     */
    public TrialInvitro toEntity(TrialInvitroRequestDTO dto) {
        if (dto == null) return null;

        TrialInvitro entity = new TrialInvitro();
        entity.setTitle(dto.getTitle());
        entity.setDetail(dto.getDetail());
        entity.setTrialOutput(dto.getTrialOutput());
        return entity;
    }

    /**
     * Entity -> ResponseDTO (조회 응답용)
     */
    public TrialInvitroResponseDTO toResponseDTO(TrialInvitro entity) {
        if (entity == null) return null;

        TrialInvitroResponseDTO dto = new TrialInvitroResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDetail(entity.getDetail());
        dto.setTrialOutput(entity.getTrialOutput());

        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        return dto;
    }
}