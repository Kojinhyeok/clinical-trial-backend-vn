package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbSurveyAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IrbSurveyAnswerRepository extends JpaRepository<IrbSurveyAnswerEntity, Long> {

    // 특정 시험에 대해 특정 유저가 작성한 답변이 있는지 조회
    Optional<IrbSurveyAnswerEntity> findByIrbTestIdAndUserId(Long irbTestId, Long userId);
    List<IrbSurveyAnswerEntity> findByIrbTestId(Long irbTestId);
}