package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbSurveyRequestDTO;
import com.clinical.dto.irb.IrbSurveyResponseDTO;
import com.clinical.service.irb.IrbSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-surveys")
@RequiredArgsConstructor
public class IrbSurveyController {

    private final IrbSurveyService irbSurveyService;
    /**
     * 1. 심사 작성 (답변 및 결과 동시 저장)
     * [POST] /api/irb-surveys
     */
    @PostMapping
    public ResponseEntity<String> createSurvey(@RequestBody IrbSurveyRequestDTO requestDTO) {
        irbSurveyService.survey(requestDTO);
        return ResponseEntity.ok("심사가 성공적으로 등록되었습니다.");
    }

    /**
     * 2. 심사 전체 조회
     * [GET] /api/irb-surveys
     */
    @GetMapping
    public ResponseEntity<List<IrbSurveyResponseDTO>> getAllSurveys() {
        return ResponseEntity.ok(irbSurveyService.findAll());
    }

    /**
     * 3. 심사 수정
     * [PUT] /api/irb-surveys/{testId}?userId={userId}
     */
    @PutMapping("/{testId}")
    public ResponseEntity<String> updateSurvey(
            @PathVariable Long testId,
            @RequestParam Long userId,
            @RequestBody IrbSurveyRequestDTO requestDTO) {
        irbSurveyService.surveyUpdate(testId, userId, requestDTO);
        return ResponseEntity.ok("심사 내용이 수정되었습니다.");
    }

    /**
     * 4. 심사 삭제
     * [DELETE] /api/irb-surveys/{testId}?userId={userId}
     */
    @DeleteMapping("/{testId}")
    public ResponseEntity<String> deleteSurvey(
            @PathVariable Long testId,
            @RequestParam Long userId) {
        irbSurveyService.delete(testId, userId);
        return ResponseEntity.ok("심사 데이터가 삭제되었습니다.");
    }
}