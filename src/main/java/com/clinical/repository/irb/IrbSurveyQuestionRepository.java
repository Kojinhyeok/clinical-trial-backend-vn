package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbSurveyQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IrbSurveyQuestionRepository extends JpaRepository<IrbSurveyQuestionEntity, Long> {

    // 특정 카테고리의 활성화된 문항들을 순서대로 조회
    List<IrbSurveyQuestionEntity> findAllByQuestionCategoryAndIsActiveOrderByQuestionOrderAsc(
            String category, int isActive);

    // 모든 활성 문항 조회
    List<IrbSurveyQuestionEntity> findAllByIsActiveOrderByQuestionOrderAsc(int isActive);
}