package com.clinical.entity.trial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시험 검색 키워드 엔티티
 * - 각 키워드 그룹에 속한 검색어 (로션, 세럼, 보습, 미백 등)
 * - 사용자가 검색할 때 사용되는 실제 키워드
 */
@Entity
@Table(name = "trial_search_keyword")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialSearchKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 키워드 그룹 ID
     * trial_keyword_group.id와 연결
     */
    @Column(name = "keyword_group_id")
    private Long keywordGroupId;

    /**
     * 검색 키워드
     * 예: "로션", "세럼", "보습", "미백", "주름개선"
     */
    @Column(name = "keyword", length = 100)
    private String keyword;

    /**
     * 생성일시
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}