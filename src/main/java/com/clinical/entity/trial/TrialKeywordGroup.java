package com.clinical.entity.trial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시험 키워드 그룹 엔티티
 * - 화장품 종류별 대분류 탭 (그룹 없이 탭만 사용)
 */
@Entity
@Table(name = "trial_keyword_group")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialKeywordGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 탭 코드 (대분류)
     * SKIN_TONER_MIST: 스킨/토너/미스트
     * ESSENCE_LOTION: 에센스/로션/앰플
     * CREAM_OIL: 크림/오일
     * MASK: 마스크팩/패치/패드
     * CLEANSING: 클렌징
     * MAKEUP: 베이스 메이크업/포인트 메이크업
     * SUN_CARE: 선크림/선케어
     * HAIR_DYE: 염모제
     * SHAMPOO_TREATMENT: 샴푸/트리트먼트
     * SCALP_HAIR_TONIC: 두피앰플/헤어토닉
     * BEAUTY_DEVICE: 미용기기
     * ETC: 기타
     */
    @Column(name = "tab_code", length = 50)
    private String tabCode;

    /**
     * 탭 이름 (한글)
     */
    @Column(name = "tab_name", length = 100)
    private String tabName;

    /**
     * 그룹명 - 탭명과 동일하게 사용 (그룹 미사용)
     */
    @Column(name = "group_name", length = 255)
    private String groupName;

    /**
     * 정렬 순서
     */
    @Column(name = "display_order")
    private Integer displayOrder;

    /**
     * 생성일시
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}