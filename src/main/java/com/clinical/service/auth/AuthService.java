package com.clinical.service.auth;

import com.clinical.dto.auth.LoginRequestDTO;
import com.clinical.dto.auth.LoginResponseDTO;
import com.clinical.entity.email.EmailTokenEntity;
import com.clinical.entity.user.UserEntity;
import com.clinical.repository.email.EmailTokenRepository;
import com.clinical.repository.user.UserRepository;
import com.clinical.service.email.EmailSender;
import com.clinical.service.email.EmailTemplateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenRepository emailTokenRepository;
    private final EmailSender emailSender;
    private final EmailTemplateService emailTemplateService;

    @Value("${email.base-url}")
    private String baseUrl;

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO dto, HttpSession session) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        user.setLastLoginAt(LocalDateTime.now());

        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userPosition", user.getPosition());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .position(user.getPosition())
                .message("로그인 성공")
                .build();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    public Long verifyUserForPasswordReset(String email, String name, String phone) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("일치하는 사용자 정보가 없습니다."));

        if (!user.getName().equals(name) || !user.getPhone().equals(phone)) {
            throw new RuntimeException("일치하는 사용자 정보가 없습니다.");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        return user.getId();
    }

    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("등록되지 않은 이메일입니다."));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        List<EmailTokenEntity> existingTokens = emailTokenRepository
                .findByEmailAndTypeAndUsedAtIsNull(email, "PASSWORD_RESET");
        for (EmailTokenEntity token : existingTokens) {
            token.setUsedAt(LocalDateTime.now());
        }
        emailTokenRepository.saveAll(existingTokens);

        String token = UUID.randomUUID().toString();
        EmailTokenEntity emailToken = EmailTokenEntity.builder()
                .token(token)
                .email(email)
                .type("PASSWORD_RESET")
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        emailTokenRepository.save(emailToken);

        String resetUrl = baseUrl + "/irb/irb_pwreset_email.html?token=" + token;
        String html = emailTemplateService.buildPasswordResetEmail(user.getName(), resetUrl);

        try {
            emailSender.send(email, "[휴먼피부임상시험센터] 비밀번호 재설정", html);
            log.info("비밀번호 재설정 이메일 발송 성공: {}", email);
        } catch (Exception e) {
            log.error("비밀번호 재설정 이메일 발송 실패: {}", email, e);
            throw new RuntimeException("이메일 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    @Transactional
    public void resetPasswordByToken(String token, String newPassword) {
        EmailTokenEntity emailToken = emailTokenRepository
                .findByTokenAndTypeAndUsedAtIsNull(token, "PASSWORD_RESET")
                .orElseThrow(() -> new RuntimeException("유효하지 않은 링크입니다."));

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("만료된 링크입니다. 비밀번호 재설정을 다시 요청해 주세요.");
        }

        UserEntity user = userRepository.findByEmail(emailToken.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));

        emailToken.setUsedAt(LocalDateTime.now());
        emailTokenRepository.save(emailToken);

        log.info("토큰 기반 비밀번호 재설정 완료: {}", emailToken.getEmail());
    }
}