package com.clinical.dto.irb;

import com.clinical.dto.file.FileResponseDTO;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IrbTestResponseDTO {
    private Long id;
    private Long userId;
    private Integer isTemp; // 0: 운영, 1: 임시저장
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long irbTestId; // IRB 시험 원글 고유 ID
    private Long irbTestIdRef; // 답글인 경우 상위글의 irb_test_id
    private Integer depth; // 0: 원글, 1: 답글 ...
    private String irbCode;
    private String status; // IN_REVIEW, COMPLETED
    private Long categoryId;
    private List<FileResponseDTO> attachedFiles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String reviewStatus; // null, "ALL_APPROVED", "REVIEW_NEEDED"
}
