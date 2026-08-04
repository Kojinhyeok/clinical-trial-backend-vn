package com.clinical.entity.irb;

import com.clinical.config.JsonConverter;
import com.clinical.config.UserEmailListConverter;
import com.clinical.dto.irb.UserEmailDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "irb_email_list")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IrbEmailListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "irb_test_id")
    private Long irbTestId;

    @Convert(converter = UserEmailListConverter.class)
    @Column(name = "user_list", columnDefinition = "json")
    private List<UserEmailDTO> userList;

    @Column(name = "email_type", length = 30)
    private String emailType; // NEW_ANSWER, NEW_POST, ANSWER_COMPLETE

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}