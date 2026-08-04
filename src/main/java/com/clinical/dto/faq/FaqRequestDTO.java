package com.clinical.dto.faq;

import com.clinical.entity.enumuration.FaqType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqRequestDTO {
    private Long id;
    private Long userId;
    private FaqType faqType;
    private String title;
    private String content;
    private Integer displayOrder;
}