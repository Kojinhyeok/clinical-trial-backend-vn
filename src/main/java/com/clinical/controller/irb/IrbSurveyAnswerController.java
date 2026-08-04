package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbSurveyAnswerDTO;
import com.clinical.service.irb.IrbSurveyAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/irb-answers")
@RequiredArgsConstructor
public class IrbSurveyAnswerController {

    private final IrbSurveyAnswerService irbSurveyAnswerService;

    /**
     * 심사 답변 저장 또는 수정 (Upsert)
     * POST /api/irb-answers
     */
    @PostMapping
    public ResponseEntity<IrbSurveyAnswerDTO> submitAnswer(@RequestBody IrbSurveyAnswerDTO dto) {
        // 이미 답변이 있으면 수정, 없으면 생성을 서비스에서 처리함
        return ResponseEntity.ok(irbSurveyAnswerService.submitAnswer(dto));
    }
    /**
     * 심사 답변 저장 또는 수정 (Upsert)
     * POST /api/irb-answers
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbSurveyAnswerDTO> update(@PathVariable Long id,@RequestBody IrbSurveyAnswerDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(irbSurveyAnswerService.submitAnswer(dto));
    }

    /**
     * 특정 심사 과제에 대한 내 답변 조회
     * GET /api/irb-answers/{irbTestId}
     */
    @GetMapping("/{irbTestId}")
    public ResponseEntity<IrbSurveyAnswerDTO> getMyAnswer(
            @PathVariable Long irbTestId,
            @RequestParam Long userId) { // 실제 환경에서는 SecurityContext의 User 활용 권장
        return ResponseEntity.ok(irbSurveyAnswerService.getAnswer(irbTestId, userId));
    }
    /**
     * 답변 삭제
     * Delete /api/irb-answers/{Id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        irbSurveyAnswerService.delete(id);
    }
}