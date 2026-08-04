package com.clinical.dto.popup;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupResponseDTO {

    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer popupOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}