package com.clinical.repository.statistics;

import com.clinical.entity.statistics.CumulativeDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CumulativeDataRepository extends JpaRepository<CumulativeDataEntity, Long> {

    Optional<CumulativeDataEntity> findByDataType(String dataType);

    List<CumulativeDataEntity> findAllByOrderByIdAsc();
}