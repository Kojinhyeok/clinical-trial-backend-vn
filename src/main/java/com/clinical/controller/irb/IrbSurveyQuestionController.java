package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbSurveyQuestionDTO;
import com.clinical.service.irb.IrbSurveyQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-questions")
@RequiredArgsConstructor
public class IrbSurveyQuestionController {

    private final IrbSurveyQuestionService questionService;

    /**
     * 카테고리별 활성 문항 리스트 조회
     * GET /api/irb-questions
     */
    @GetMapping
    public ResponseEntity<List<IrbSurveyQuestionDTO>> getALl() {
        return ResponseEntity.ok(questionService.getAll());
    }

    /**
     * 카테고리별 활성 문항 리스트 조회
     * GET /api/irb-questions/BASIC
     */
    @GetMapping("/{category}")
    public ResponseEntity<List<IrbSurveyQuestionDTO>> getQuestions(@PathVariable String category) {
        return ResponseEntity.ok(questionService.getQuestionsByCategory(category));
    }

    /**
     * 신규 문항 등록
     * POST /api/irb-questions
     */
    @PostMapping
    public ResponseEntity<IrbSurveyQuestionDTO> createQuestion(@RequestBody IrbSurveyQuestionDTO dto) {
        return ResponseEntity.ok(questionService.createQuestion(dto));
    }

    /**
     * 문항 수정
     * PUT /api/irb-questions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbSurveyQuestionDTO> updateQuestion(
            @PathVariable Long id,
            @RequestBody IrbSurveyQuestionDTO dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    /**
     * 문항 삭제
     * DELETE /api/irb-questions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}