package com.clinical.dto.irb;

import com.clinical.dto.file.FileRequestDTO;
import com.clinical.entity.enumuration.IrbStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IrbTestRequestDTO {
    private Long id;
    private Long userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long categoryId;
    private IrbStatus status; // IN_REVIEW, COMPLETED
    private List<UserEmailDTO> emails;
    private Integer isTemp; // 0: 운영, 1: 임시저장
    private Long irbTestId; // IRB 시험 원글 고유 ID
    private Long irbTestIdRef; // 답글인 경우 상위글의 irb_test_id
    private Integer depth; // 0: 원글, 1: 답글 ...
    private String irbCode;
    private List<FileRequestDTO> files;
    private List<FileRequestDTO> removeFiles;
}
