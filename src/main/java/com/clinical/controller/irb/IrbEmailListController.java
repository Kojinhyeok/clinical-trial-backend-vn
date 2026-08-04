package com.clinical.controller.irb;

import com.clinical.dto.irb.IrbEmailListResponseDTO;
import com.clinical.dto.irb.UserEmailDTO;
import com.clinical.entity.enumuration.EmailType;
import com.clinical.service.irb.IrbEmailListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/irb-emails")
@RequiredArgsConstructor
public class IrbEmailListController {

    private final IrbEmailListService irbEmailListService;

    /**
     * 특정 IRB 시험에 대한 이메일 발송 이력 조회
     * GET /api/irb-emails/logs/{irbTestId}
     */
    @GetMapping("/logs/{irbTestId}")
    public ResponseEntity<List<IrbEmailListResponseDTO>> getEmailLogs(@PathVariable Long irbTestId) {
        List<IrbEmailListResponseDTO> logs = irbEmailListService.getLogsByTestId(irbTestId);
        return ResponseEntity.ok(logs);
    }

    /**
     * 이메일 발송 리스트 기록 수동 저장 (필요 시)
     * POST /api/irb-emails/logs/{irbTestId}
     */
    @PostMapping("/logs/{irbTestId}")
    public ResponseEntity<IrbEmailListResponseDTO> createEmailLog(
            @PathVariable Long irbTestId,
            @RequestBody List<UserEmailDTO> users,
            @RequestParam EmailType type) {
        IrbEmailListResponseDTO response = irbEmailListService.saveEmailLog(irbTestId, users, type);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 IRB 시험의 이메일 이력 삭제
     * DELETE /api/irb-emails/{irbId}
     */
    @DeleteMapping("/{irbId}")
    public ResponseEntity<Void> deleteEmailLogs(@PathVariable Long irbId) {
        irbEmailListService.delete(irbId);
        return ResponseEntity.noContent().build();
    }
}