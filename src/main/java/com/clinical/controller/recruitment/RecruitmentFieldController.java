package com.clinical.controller.recruitment;

import com.clinical.dto.recruitment.RecruitmentFieldResponseDTO;
import com.clinical.repository.recruitment.RecruitmentFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recruitment-fields")
@RequiredArgsConstructor
public class RecruitmentFieldController {

    private final RecruitmentFieldRepository fieldRepository;

    @GetMapping
    public ResponseEntity<List<RecruitmentFieldResponseDTO>> getAllFields() {
        List<RecruitmentFieldResponseDTO> fields = fieldRepository.findAll().stream()
                .map(f -> RecruitmentFieldResponseDTO.builder()
                        .id(f.getId())
                        .fieldCode(f.getFieldCode())
                        .fieldName(f.getFieldName())
                        .createdAt(f.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(fields);
    }
}