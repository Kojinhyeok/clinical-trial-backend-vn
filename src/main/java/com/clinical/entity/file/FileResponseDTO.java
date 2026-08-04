package com.clinical.dto.file;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileResponseDTO {
    private Long id;
    private String entityType;
    private Long entityId;
    private String fileCategory;
    private String originalFilename;
    private Long fileSize;
    private String s3Url;
    private String downloadUrl;
    private String uploadUrl;
    private LocalDateTime createdAt;
}