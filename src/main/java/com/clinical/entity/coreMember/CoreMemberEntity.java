package com.clinical.entity.coreMember;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoreMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String affiliation;

    @Column(length = 100)
    private String position;

    @Column(length = 500)
    private String slogan;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}