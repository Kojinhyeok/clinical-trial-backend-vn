package com.clinical.dto.coreMember;

import lombok.*;

public class CoreMemberRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        private String name;
        private String affiliation;
        private String position;
        private String slogan;
        private String detail;
        private Integer displayOrder;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String name;
        private String affiliation;
        private String position;
        private String slogan;
        private String detail;
        private Integer displayOrder;
    }
}