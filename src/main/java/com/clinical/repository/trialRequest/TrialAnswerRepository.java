package com.clinical.repository.trialRequest;

import com.clinical.entity.trialRequest.TrialAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrialAnswerRepository extends JpaRepository<TrialAnswer, Long> {

    List<TrialAnswer> findByQuestionId(Long questionId);

    List<TrialAnswer> findByQuestionIdOrderByCreatedAtDesc(Long questionId);

    Optional<TrialAnswer> findFirstByQuestionIdOrderByCreatedAtDesc(Long questionId);

    boolean existsByQuestionId(Long questionId);

    long countByQuestionId(Long questionId);

    List<TrialAnswer> findByUserId(Long userId);

    long countByUserId(Long userId);

    Optional<TrialAnswer> findByQuestionIdAndUserId(Long questionId, Long userId);

    void deleteByQuestionId(Long questionId);

    @Query("SELECT DISTINCT ta.questionId FROM TrialAnswer ta")
    List<Long> findDistinctQuestionIds();
}