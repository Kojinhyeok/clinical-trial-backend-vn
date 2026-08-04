package com.clinical.dto.file;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileRequestDTO {
    private Long id;
    private String entityType;
    private Long entityId;
    private String fileCategory;
    private Long uploadedBy;
    private String originalFilename;
    private Long fileSize;
    private String mimeType;
}