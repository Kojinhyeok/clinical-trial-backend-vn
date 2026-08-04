package com.clinical.dto.irb;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IrbCategoryDTO {
    private Long id;
    private String categoryCode; // 예: DOC, ETH
    private String category;     // 예: 서류심사, 윤리심사
}