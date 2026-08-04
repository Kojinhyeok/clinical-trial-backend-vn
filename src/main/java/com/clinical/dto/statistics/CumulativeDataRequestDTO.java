package com.clinical.dto.statistics;

import lombok.*;

public class CumulativeDataRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String dataType;
        private Long dataCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String dataType;
        private Long dataCount;
    }
}