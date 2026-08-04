package com.clinical.controller.popup;

import com.clinical.dto.popup.PopupRequestDTO;
import com.clinical.dto.popup.PopupResponseDTO;
import com.clinical.service.popup.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @GetMapping
    public ResponseEntity<List<PopupResponseDTO>> getActivePopups() {
        return ResponseEntity.ok(popupService.getActivePopups());
    }

    @GetMapping("/all")
    public ResponseEntity<List<PopupResponseDTO>> getAllPopups() {
        return ResponseEntity.ok(popupService.getAllPopups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopupResponseDTO> getPopupById(@PathVariable Long id) {
        return ResponseEntity.ok(popupService.getPopupById(id));
    }

    @PostMapping
    public ResponseEntity<PopupResponseDTO> createPopup(@RequestBody PopupRequestDTO.Create request) {
        return ResponseEntity.ok(popupService.createPopup(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopupResponseDTO> updatePopup(@PathVariable Long id, @RequestBody PopupRequestDTO.Update request) {
        return ResponseEntity.ok(popupService.updatePopup(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePopup(@PathVariable Long id) {
        popupService.deletePopup(id);
        return ResponseEntity.ok().build();
    }
}