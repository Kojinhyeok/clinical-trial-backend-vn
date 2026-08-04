package com.clinical.dto.irb;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class IrbEmailListResponseDTO {
    private Long id;
    private Long irbTestId;
    private List<UserEmailDTO> userList;
    private String emailType;
    private LocalDateTime createdAt;
}