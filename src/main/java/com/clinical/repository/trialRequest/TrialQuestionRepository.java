package com.clinical.repository.trialRequest;

import com.clinical.entity.trialRequest.TrialQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrialQuestionRepository extends JpaRepository<TrialQuestion, Long> {

    List<TrialQuestion> findByPhoneOrderByCreatedAtDesc(String phone);

    List<TrialQuestion> findAllByOrderByCreatedAtDesc();

    List<TrialQuestion> findByStatusOrderByCreatedAtDesc(String status);

    Optional<TrialQuestion> findByIdAndPassword(Long id, String password);

    boolean existsByIdAndPassword(Long id, String password);

    long countByStatus(String status);

    @Query(value = """
        SELECT *
        FROM trial_question
        ORDER BY created_at DESC
        LIMIT 5
        """, nativeQuery = true)
    List<TrialQuestion> findTop5ByOrderByCreatedAtDesc();
}