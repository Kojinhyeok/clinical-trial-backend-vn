package com.clinical.controller.trialRequest;

import com.clinical.dto.trialRequest.TrialFilesRequestDTO;
import com.clinical.dto.trialRequest.TrialFilesResponseDTO;
import com.clinical.service.trialRequest.TrialFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-files")
@RequiredArgsConstructor
public class TrialFilesController {

    private final TrialFilesService trialFilesService;

    @GetMapping
    public ResponseEntity<List<TrialFilesResponseDTO>> getAllFiles() {
        List<TrialFilesResponseDTO> response = trialFilesService.getAllFiles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<TrialFilesResponseDTO>> getAllFilesForAdmin() {
        List<TrialFilesResponseDTO> response = trialFilesService.getAllFilesForAdmin();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrialFilesResponseDTO>> searchFiles(
            @RequestParam(required = false, defaultValue = "subject") String field,
            @RequestParam String keyword) {

        List<TrialFilesResponseDTO> response;

        if ("content".equalsIgnoreCase(field)) {
            response = trialFilesService.searchByContent(keyword);
        } else {
            response = trialFilesService.searchByTitle(keyword);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialFilesResponseDTO> getFileDetail(@PathVariable Long id) {
        TrialFilesResponseDTO response = trialFilesService.getFileDetail(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TrialFilesResponseDTO> createFile(@RequestBody TrialFilesRequestDTO request) {
        TrialFilesResponseDTO response = trialFilesService.createFile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialFilesResponseDTO> updateFile(
            @PathVariable Long id,
            @RequestBody TrialFilesRequestDTO request) {
        TrialFilesResponseDTO response = trialFilesService.updateFile(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        trialFilesService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}