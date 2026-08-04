package com.clinical.dto.user;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String position;
    private String status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}