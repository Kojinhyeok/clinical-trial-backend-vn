package com.clinical.repository.trial;

import com.clinical.entity.trial.TrialSafety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface TrialSafetyRepository extends JpaRepository<TrialSafety, Long> {
    List<TrialSafety> findAll(Sort sort);
}
