package com.clinical.dto.faq;

import com.clinical.entity.enumuration.FaqType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FaqResponseDTO {
    private Long id;
    private Long userId;
    private FaqType faqType;
    private String title;
    private String content;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}