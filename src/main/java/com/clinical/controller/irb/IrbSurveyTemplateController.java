package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbSurveyTemplateDTO;
import com.clinical.service.irb.IrbSurveyTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-templates")
@RequiredArgsConstructor
public class IrbSurveyTemplateController {

    private final IrbSurveyTemplateService templateService;

    /**
     * 심사 템플릿 신규 등록
     * POST /api/irb-templates
     */
    @PostMapping
    public ResponseEntity<IrbSurveyTemplateDTO> createTemplate(@RequestBody IrbSurveyTemplateDTO dto) {
        return ResponseEntity.ok(templateService.createTemplate(dto));
    }

    /**
     * 특정 IRB 과제에 할당된 템플릿 조회 (심사 화면 로딩 시 사용)
     * GET /api/irb-templates/{irbTestId}?surveyName=BASIC
     */
    @GetMapping("/{irbTestId}")
    public ResponseEntity<IrbSurveyTemplateDTO> getTemplate(
            @PathVariable Long irbTestId,
            @RequestParam String surveyName) {
        return ResponseEntity.ok(templateService.getTemplate(irbTestId, surveyName));
    }
    /**
     * 템플릿 전체 조회
     * GET /api/irb-templates
     */
    @GetMapping
    public ResponseEntity<List<IrbSurveyTemplateDTO>> getAll() {
        return ResponseEntity.ok(templateService.getTemplatesAll());
    }

    /**
     * 기존 템플릿 수정 (문항 리스트 업데이트 등)
     * PUT /api/irb-templates/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<IrbSurveyTemplateDTO> updateTemplate(
            @PathVariable Long id,
            @RequestBody IrbSurveyTemplateDTO dto) {
        return ResponseEntity.ok(templateService.updateTemplate(id, dto));
    }

    /**
     * 기존 템플릿 삭제
     * PUT /api/irb-templates/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        templateService.delete(id);
    }
}