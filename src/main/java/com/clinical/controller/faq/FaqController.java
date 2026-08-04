package com.clinical.controller.faq;

import com.clinical.dto.faq.FaqRequestDTO;
import com.clinical.dto.faq.FaqResponseDTO;
import com.clinical.entity.enumuration.FaqType;
import com.clinical.service.faq.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    @GetMapping("/participation")
    public ResponseEntity<List<FaqResponseDTO>> findParticipationAll(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(faqService.getFaqs(FaqType.PARTICIPATION, searchType, keyword));
    }

    @GetMapping("/request")
    public ResponseEntity<List<FaqResponseDTO>> findRequestAll(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(faqService.getFaqs(FaqType.REQUEST, searchType, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaqResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFaqById(id));
    }

    @PostMapping
    public ResponseEntity<FaqResponseDTO> create(@RequestBody FaqRequestDTO faqRequestDTO){
        return ResponseEntity.ok(faqService.createFaq(faqRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FaqResponseDTO> update(@PathVariable Long id,@RequestBody FaqRequestDTO faqRequestDTO){
        return ResponseEntity.ok(faqService.updateFaq(id,faqRequestDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        faqService.deleteFaq(id);
    }
}