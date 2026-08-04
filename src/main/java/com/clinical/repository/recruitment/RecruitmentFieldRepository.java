package com.clinical.repository.recruitment;

import com.clinical.entity.recruitment.RecruitmentField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentFieldRepository extends JpaRepository<RecruitmentField, Long> {

    List<RecruitmentField> findAll();

    Optional<RecruitmentField> findByFieldCode(String fieldCode);

    Optional<RecruitmentField> findByFieldName(String fieldName);

    boolean existsByFieldCode(String fieldCode);
}