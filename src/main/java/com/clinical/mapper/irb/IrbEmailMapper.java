package com.clinical.mapper.irb;

import com.clinical.dto.irb.IrbEmailListResponseDTO;
import com.clinical.dto.irb.UserEmailDTO;
import com.clinical.entity.irb.IrbEmailListEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IrbEmailMapper {

    public IrbEmailListResponseDTO toDto(IrbEmailListEntity entity) {
        if (entity == null) return null;
        return IrbEmailListResponseDTO.builder()
                .id(entity.getId())
                .irbTestId(entity.getIrbTestId())
                .userList(entity.getUserList())
                .emailType(entity.getEmailType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public IrbEmailListEntity toEntity(Long irbTestId, List<UserEmailDTO> userList, String emailType) {
        return IrbEmailListEntity.builder()
                .irbTestId(irbTestId)
                .userList(userList)
                .emailType(emailType)
                .build();
    }
}