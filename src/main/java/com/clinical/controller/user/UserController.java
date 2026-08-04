package com.clinical.controller.user;

import com.clinical.dto.user.UserRequestDTO;
import com.clinical.dto.user.UserResponseDTO;
import com.clinical.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/professors")
    public ResponseEntity<List<UserResponseDTO>> getProfessors() {
        return ResponseEntity.ok(userService.getProfessors());
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.inviteUser(request);

            Map<String, Object> result = new HashMap<>();
            result.put("user", response);
            result.put("message", "초대 이메일이 발송되었습니다.");

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{id}/resend-invite")
    public ResponseEntity<?> resendInviteEmail(@PathVariable Long id) {
        try {
            userService.resendInviteEmail(id);
            return ResponseEntity.ok(Map.of("message", "초대 이메일이 재발송되었습니다."));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/signup-by-token")
    public ResponseEntity<?> getSignupUserByToken(@RequestParam String token) {
        try {
            UserResponseDTO response = userService.getSignupUserByToken(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/signup-by-token")
    public ResponseEntity<?> completeSignupByToken(
            @RequestParam String token,
            @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.completeSignupByToken(token, request);

            Map<String, Object> result = new HashMap<>();
            result.put("user", response);
            result.put("message", "회원가입이 완료되었습니다.");

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/signup/{id}")
    public ResponseEntity<?> getSignupUser(@PathVariable Long id) {
        try {
            UserResponseDTO response = userService.getSignupUser(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/signup/{id}")
    public ResponseEntity<?> completeSignup(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userService.completeSignup(id, request);

            Map<String, Object> result = new HashMap<>();
            result.put("user", response);
            result.put("message", "회원가입이 완료되었습니다.");

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}