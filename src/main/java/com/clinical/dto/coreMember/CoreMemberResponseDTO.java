package com.clinical.dto.coreMember;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreMemberResponseDTO {
    private Long id;
    private String name;
    private String affiliation;
    private String position;
    private String slogan;
    private String detail;
    private Integer displayOrder;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}