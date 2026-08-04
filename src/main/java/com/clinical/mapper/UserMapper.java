package com.clinical.mapper;

import com.clinical.dto.user.UserRequestDTO;
import com.clinical.dto.user.UserResponseDTO;
import com.clinical.entity.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequestDTO dto) {
        if (dto == null) return null;

        return UserEntity.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .phone(dto.getPhone())
                .position(dto.getPosition())
                .status("ACTIVE")
                .build();
    }

    public UserResponseDTO toDto(UserEntity entity) {
        if (entity == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setPosition(entity.getPosition());
        dto.setStatus(entity.getStatus());
        dto.setLastLoginAt(entity.getLastLoginAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}