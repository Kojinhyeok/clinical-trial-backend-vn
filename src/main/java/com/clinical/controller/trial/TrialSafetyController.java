package com.clinical.controller.trial;

import com.clinical.dto.trialSafety.TrialSafetyRequestDTO;
import com.clinical.dto.trialSafety.TrialSafetyResponseDTO;
import com.clinical.service.trial.TrialSafetyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-safety")
@RequiredArgsConstructor
public class TrialSafetyController {

    private final TrialSafetyService safetyService;

    /**
     * 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<TrialSafetyResponseDTO>> getAll() {
        return ResponseEntity.ok(safetyService.getAllItems());
    }

    /**
     * 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialSafetyResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(safetyService.getItemById(id));
    }

    /**
     * 등록 (POST /api/trial-safety?userId=1)
     */
    @PostMapping
    public ResponseEntity<TrialSafetyResponseDTO> register(
            @RequestBody TrialSafetyRequestDTO dto,
            @RequestParam Long userId) {
        TrialSafetyResponseDTO response = safetyService.saveItem(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 수정 (PUT /api/trial-safety/{id}?userId=1)
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrialSafetyResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TrialSafetyRequestDTO dto,
            @RequestParam Long userId) {
        dto.setId(id);
        TrialSafetyResponseDTO response = safetyService.updateItem(dto, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        safetyService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }


}