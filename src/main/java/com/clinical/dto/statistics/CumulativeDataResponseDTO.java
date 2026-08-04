package com.clinical.dto.statistics;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CumulativeDataResponseDTO {
    private Long id;
    private String dataType;
    private Long dataCount;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}