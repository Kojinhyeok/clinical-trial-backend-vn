package com.clinical.repository.email;

import com.clinical.entity.email.EmailTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailTokenEntity, Long> {

    Optional<EmailTokenEntity> findByTokenAndTypeAndUsedAtIsNull(String token, String type);

    List<EmailTokenEntity> findByEmailAndTypeAndUsedAtIsNull(String email, String type);
}