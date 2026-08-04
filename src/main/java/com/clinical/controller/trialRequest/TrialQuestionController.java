package com.clinical.controller.trialRequest;

import com.clinical.dto.trialRequest.TrialQuestionRequestDTO;
import com.clinical.dto.trialRequest.TrialQuestionResponseDTO;
import com.clinical.service.trialRequest.TrialQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trial-questions")
@RequiredArgsConstructor
public class TrialQuestionController {

    private final TrialQuestionService trialQuestionService;

    @PostMapping
    public ResponseEntity<TrialQuestionResponseDTO.DetailResponse> create(
            @RequestBody TrialQuestionRequestDTO.Create request) {
        TrialQuestionResponseDTO.DetailResponse response = trialQuestionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TrialQuestionResponseDTO.ListResponse>> getAll() {
        List<TrialQuestionResponseDTO.ListResponse> response = trialQuestionService.getAllQuestions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-phone")
    public ResponseEntity<List<TrialQuestionResponseDTO.ListResponse>> getByPhone(
            @RequestParam String phone) {
        List<TrialQuestionResponseDTO.ListResponse> response = trialQuestionService.getQuestionsByPhone(phone);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Map<String, Boolean>> verifyPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        boolean verified = trialQuestionService.verifyPassword(id, password);
        return ResponseEntity.ok(Map.of("verified", verified));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialQuestionResponseDTO.DetailResponse> getById(@PathVariable Long id) {
        TrialQuestionResponseDTO.DetailResponse response = trialQuestionService.getQuestionById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialQuestionResponseDTO.DetailResponse> update(
            @PathVariable Long id,
            @RequestBody TrialQuestionRequestDTO.Update request) {
        TrialQuestionResponseDTO.DetailResponse response = trialQuestionService.updateQuestion(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam String password) {
        trialQuestionService.deleteQuestion(id, password);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<TrialQuestionResponseDTO.DetailResponse> createAnswer(
            @PathVariable Long id,
            @RequestBody TrialQuestionRequestDTO.CreateAnswer request) {
        TrialQuestionResponseDTO.DetailResponse response = trialQuestionService.createAnswer(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<TrialQuestionResponseDTO.DetailResponse>> getAllForAdmin() {
        List<TrialQuestionResponseDTO.DetailResponse> response = trialQuestionService.getAllQuestionsForAdmin();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> adminDelete(@PathVariable Long id) {
        trialQuestionService.adminDeleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<TrialQuestionResponseDTO.DetailResponse> updateAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody Map<String, String> request) {
        TrialQuestionResponseDTO.DetailResponse response =
                trialQuestionService.updateAnswer(questionId, answerId, request.get("content"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable Long questionId,
            @PathVariable Long answerId) {
        trialQuestionService.deleteAnswer(questionId, answerId);
        return ResponseEntity.noContent().build();
    }
}