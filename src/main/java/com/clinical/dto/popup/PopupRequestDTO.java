package com.clinical.dto.popup;

import lombok.*;

public class PopupRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String title;
        private Integer popupOrder;
        private Boolean isActive;
        private String linkUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private Integer popupOrder;
        private Boolean isActive;
        private String linkUrl;
    }
}