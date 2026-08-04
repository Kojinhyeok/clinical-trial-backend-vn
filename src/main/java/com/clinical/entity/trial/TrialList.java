package com.clinical.entity.trial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 시험 목록 엔티티 (schema_v1_DDL.sql의 trial_list 테이블과 매핑)
 * - 실제 진행된 임상시험 결과 정보
 * - 유효성 평가 페이지에서 표시되는 시험 항목
 * - 전/후 이미지는 files 테이블과 연동 (entity_type='trial_list', file_category='BEFORE'/'AFTER')
 */
@Entity
@Table(name = "trial_list")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 키워드 그룹 ID
     * trial_keyword_group.id와 연결
     * 어떤 제품군의 시험인지 구분 (로션/세럼/아이크림 등)
     */
    @Column(name = "keyword_group_id")
    private Long keywordGroupId;

    /**
     * 시험 제목
     * 예: "보습 세럼 4주 효능 평가"
     */
    @Column(name = "trial_title", length = 100)
    private String trialTitle;

    /**
     * 시험 부제
     * 예: "건조한 피부 개선 효과"
     */
    @Column(name = "trial_subtitle", length = 100)
    private String trialSubtitle;

    /**
     * 시험 상세 설명
     * 예: "20대-40대 여성 30명 대상, 4주간 아침/저녁 2회 도포"
     */
    @Column(name = "trial_description", length = 255)
    private String trialDescription;

    /**
     * 시험 인원
     * 예: "30명", "50명"
     */
    @Column(name = "trial_personnel", length = 100)
    private String trialPersonnel;

    /**
     * 필요 시료량
     * 예: "50ml", "100g"
     */
    @Column(name = "trial_required_sample", length = 100)
    private String trialRequiredSample;

    /**
     * 보고서 수령일
     * 예: "시료 입고 후 약 4주", "협의 후 결정"
     */
    @Column(name = "trial_report_date", length = 100)
    private String trialReportDate;

    /**
     * 보고서 수령일 상세
     * 예: "시험 종료일로부터 2주 후 (변동 가능)"
     */
    @Column(name = "trial_report_date_sub", length = 100)
    private String trialReportDateSub;

    /**
     * 시험 측정 시점
     * 예: "4주 후", "8주 후", "사용 직후"
     */
    @Column(name = "trial_time_point", length = 100)
    private String trialTimePoint;

    /**
     * 시험 부위
     * 예: "얼굴", "팔 안쪽", "눈가", "두피"
     */
    @Column(name = "trial_part", length = 100)
    private String trialPart;

    /**
     * 산출물 (결과 유형)
     * ⚠️ "효능 평가"가 아니라 "수치", "이미지", "수치+이미지" 등
     * 예: "수치", "이미지", "수치+이미지", "설문조사"
     */
    @Column(name = "trial_result_type", length = 100)
    private String trialResultType;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}