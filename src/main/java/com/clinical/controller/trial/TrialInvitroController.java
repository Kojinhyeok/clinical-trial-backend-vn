package com.clinical.controller.trial;

import com.clinical.dto.trialInvitro.TrialInvitroRequestDTO;
import com.clinical.dto.trialInvitro.TrialInvitroResponseDTO;
import com.clinical.service.trial.TrialInvitroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-invitro")
@RequiredArgsConstructor
public class TrialInvitroController {

    private final TrialInvitroService invitroService;

    /**
     * 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<TrialInvitroResponseDTO>> getAll() {
        return ResponseEntity.ok(invitroService.getAllItems());
    }

    /**
     * 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrialInvitroResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(invitroService.getItemById(id));
    }

    /**
     * 등록 (POST /api/trial-invitro?userId=1)
     */
    @PostMapping
    public ResponseEntity<TrialInvitroResponseDTO> register(
            @RequestBody TrialInvitroRequestDTO dto,
            @RequestParam Long userId) {
        TrialInvitroResponseDTO response = invitroService.saveItem(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 수정 (PUT /api/trial-invitro/{id}?userId=1)
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrialInvitroResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TrialInvitroRequestDTO dto,
            @RequestParam Long userId) {
        dto.setId(id);
        TrialInvitroResponseDTO response = invitroService.updateItem(dto, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invitroService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}