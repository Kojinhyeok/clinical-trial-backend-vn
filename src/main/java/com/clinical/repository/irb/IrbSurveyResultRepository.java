package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbSurveyResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IrbSurveyResultRepository extends JpaRepository<IrbSurveyResultEntity, Long> {

    /**
     * 특정 IRB 시험에 대해 특정 심사자(User)가 내린 결과 조회
     * (중복 심사 방지 및 수정용)
     */
    Optional<IrbSurveyResultEntity> findByIrbTestIdAndUserId(Long irbTestId, Long userId);

    /**
     * 특정 IRB 시험의 모든 심사 결과 목록 조회 (최종 승인 여부 판단용)
     */
    List<IrbSurveyResultEntity> findAllByIrbTestId(Long irbTestId);
}