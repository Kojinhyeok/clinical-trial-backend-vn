package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IrbCategoryRepository extends JpaRepository<IrbCategoryEntity, Long> {
    // 코드값으로 카테고리 단건 조회
    Optional<IrbCategoryEntity> findByCategoryCode(String categoryCode);
}