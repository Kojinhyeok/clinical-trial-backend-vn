package com.clinical.controller.notification;

import com.clinical.dto.notification.TrialNotificationRequestDTO;
import com.clinical.dto.notification.TrialNotificationResponseDTO;
import com.clinical.service.notification.TrialNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final TrialNotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<TrialNotificationResponseDTO>> getList() {
        List<TrialNotificationResponseDTO> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrialNotificationResponseDTO>> getIsNotTempList() {
        List<TrialNotificationResponseDTO> notifications = notificationService.getIsNotTempNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrialNotificationResponseDTO>> search(
            @RequestParam String field,
            @RequestParam String keyword) {
        List<TrialNotificationResponseDTO> notifications = notificationService.searchNotifications(field, keyword);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialNotificationResponseDTO> getOne(@PathVariable Long id) {
        TrialNotificationResponseDTO notification = notificationService.getNotificationDetail(id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping
    public ResponseEntity<TrialNotificationResponseDTO> write(@RequestBody TrialNotificationRequestDTO dto) {
        TrialNotificationResponseDTO created = notificationService.createNotification(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialNotificationResponseDTO> update(@PathVariable Long id, @RequestBody TrialNotificationRequestDTO dto) {
        TrialNotificationResponseDTO updated = notificationService.updateNotification(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}