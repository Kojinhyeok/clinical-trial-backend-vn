package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbSurveyTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrbSurveyTemplateRepository extends JpaRepository<IrbSurveyTemplateEntity, Long> {

    // 특정 IRB 시험에 할당된 템플릿 목록 조회
    List<IrbSurveyTemplateEntity> findAllByIrbTestId(Long irbTestId);

    // 특정 시험의 특정 타입(BASIC/EXTRA) 템플릿 조회
    Optional<IrbSurveyTemplateEntity> findByIrbTestIdAndSurveyName(Long irbTestId, String surveyName);
}