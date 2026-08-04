package com.clinical.service.user;

import com.clinical.dto.user.UserRequestDTO;
import com.clinical.dto.user.UserResponseDTO;
import com.clinical.entity.email.EmailTokenEntity;
import com.clinical.entity.enumuration.UserPosition;
import com.clinical.entity.user.UserEntity;
import com.clinical.mapper.UserMapper;
import com.clinical.repository.email.EmailTokenRepository;
import com.clinical.repository.user.UserRepository;
import com.clinical.service.email.EmailSender;
import com.clinical.service.email.EmailTemplateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenRepository emailTokenRepository;
    private final EmailSender emailSender;
    private final EmailTemplateService emailTemplateService;

    @Value("${email.base-url}")
    private String baseUrl;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserResponseDTO> getProfessors() {
        List<UserEntity> professors = userRepository.findAllByPosition(UserPosition.PROFESSOR.toString());
        return professors.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDTO getUserInfo(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return userMapper.toDto(user);
    }

    public UserResponseDTO inviteUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        UserEntity user = UserEntity.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .status("SUSPENDED")
                .build();

        UserEntity savedUser = userRepository.save(user);

        sendInviteEmail(savedUser);

        return userMapper.toDto(savedUser);
    }

    public void resendInviteEmail(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!"SUSPENDED".equals(user.getStatus())) {
            throw new RuntimeException("이미 회원가입이 완료된 사용자입니다.");
        }

        List<EmailTokenEntity> existingTokens = emailTokenRepository
                .findByEmailAndTypeAndUsedAtIsNull(user.getEmail(), "INVITE");
        for (EmailTokenEntity token : existingTokens) {
            token.setUsedAt(LocalDateTime.now());
        }
        emailTokenRepository.saveAll(existingTokens);

        sendInviteEmail(user);
    }

    public UserResponseDTO getSignupUserByToken(String token) {
        EmailTokenEntity emailToken = emailTokenRepository
                .findByTokenAndTypeAndUsedAtIsNull(token, "INVITE")
                .orElseThrow(() -> new RuntimeException("유효하지 않은 초대 링크입니다."));

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("만료된 초대 링크입니다. 관리자에게 재발송을 요청해 주세요.");
        }

        UserEntity user = userRepository.findByEmail(emailToken.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!"SUSPENDED".equals(user.getStatus())) {
            throw new RuntimeException("이미 회원가입이 완료된 사용자입니다.");
        }

        return userMapper.toDto(user);
    }

    public UserResponseDTO getSignupUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!"SUSPENDED".equals(user.getStatus())) {
            throw new RuntimeException("이미 회원가입이 완료된 사용자입니다.");
        }

        return userMapper.toDto(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("이미 등록된 이메일입니다.");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getPosition() != null) user.setPosition(dto.getPosition());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());

        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO completeSignup(Long id, UserRequestDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!"SUSPENDED".equals(user.getStatus())) {
            throw new RuntimeException("이미 회원가입이 완료된 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        user.setPassword(encodedPassword);
        user.setPosition(dto.getPosition());
        user.setStatus("ACTIVE");

        return userMapper.toDto(user);
    }

    public UserResponseDTO completeSignupByToken(String token, UserRequestDTO dto) {
        EmailTokenEntity emailToken = emailTokenRepository
                .findByTokenAndTypeAndUsedAtIsNull(token, "INVITE")
                .orElseThrow(() -> new RuntimeException("유효하지 않은 초대 링크입니다."));

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("만료된 초대 링크입니다.");
        }

        UserEntity user = userRepository.findByEmail(emailToken.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!"SUSPENDED".equals(user.getStatus())) {
            throw new RuntimeException("이미 회원가입이 완료된 사용자입니다.");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPosition(dto.getPosition());
        user.setStatus("ACTIVE");

        emailToken.setUsedAt(LocalDateTime.now());
        emailTokenRepository.save(emailToken);

        return userMapper.toDto(user);
    }

    private void sendInviteEmail(UserEntity user) {
        String token = UUID.randomUUID().toString();

        EmailTokenEntity emailToken = EmailTokenEntity.builder()
                .token(token)
                .email(user.getEmail())
                .type("INVITE")
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        emailTokenRepository.save(emailToken);

        String signupUrl = baseUrl + "/signup.html?token=" + token;
        String html = emailTemplateService.buildInviteEmail(user.getName(), signupUrl);

        try {
            emailSender.send(user.getEmail(), "[휴먼피부임상시험센터] 회원가입 초대", html);
            log.info("초대 이메일 발송 성공: {}", user.getEmail());
        } catch (Exception e) {
            log.error("초대 이메일 발송 실패: {}", user.getEmail(), e);
        }
    }
}