package com.clinical.controller.auth;

import com.clinical.dto.auth.LoginRequestDTO;
import com.clinical.dto.auth.LoginResponseDTO;
import com.clinical.dto.auth.PasswordResetRequestDTO;
import com.clinical.dto.auth.PasswordResetDTO;
import com.clinical.dto.user.UserResponseDTO;
import com.clinical.service.auth.AuthService;
import com.clinical.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request,
            HttpSession session) {
        try {
            LoginResponseDTO response = authService.login(request, session);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponseDTO.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        authService.logout(session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> check(HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLoggedIn", authService.isLoggedIn(session));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "로그인되지 않았습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        try {
            UserResponseDTO user = userService.getUserInfo(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/password-reset/verify")
    public ResponseEntity<?> verifyForPasswordReset(@RequestBody PasswordResetRequestDTO request) {
        try {
            Long userId = authService.verifyUserForPasswordReset(
                    request.getEmail(),
                    request.getName(),
                    request.getPhone()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("message", "본인 인증이 완료되었습니다.");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/password-reset/{userId}")
    public ResponseEntity<?> resetPassword(
            @PathVariable Long userId,
            @RequestBody PasswordResetDTO request) {
        try {
            if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "비밀번호가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(error);
            }

            authService.resetPassword(userId, request.getNewPassword());

            Map<String, String> response = new HashMap<>();
            response.put("message", "비밀번호가 변경되었습니다.");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/password-reset/send-email")
    public ResponseEntity<?> sendPasswordResetEmail(@RequestBody Map<String, String> request) {
        log.info("=== password-reset/send-email 호출됨, request: {} ===", request);
        try {
            String email = request.get("email");
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "이메일을 입력해 주세요."));
            }

            authService.sendPasswordResetEmail(email);
            log.info("=== password-reset/send-email 완료 ===");

            return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 링크가 이메일로 발송되었습니다."));
        } catch (Exception e) {
            log.error("=== password-reset/send-email 에러 ===", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/password-reset/by-token")
    public ResponseEntity<?> resetPasswordByToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            String newPasswordConfirm = request.get("newPasswordConfirm");

            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "유효하지 않은 요청입니다."));
            }
            if (newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "새 비밀번호를 입력해 주세요."));
            }
            if (!newPassword.equals(newPasswordConfirm)) {
                return ResponseEntity.badRequest().body(Map.of("message", "비밀번호가 일치하지 않습니다."));
            }

            authService.resetPasswordByToken(token, newPassword);

            return ResponseEntity.ok(Map.of("message", "비밀번호가 변경되었습니다."));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}