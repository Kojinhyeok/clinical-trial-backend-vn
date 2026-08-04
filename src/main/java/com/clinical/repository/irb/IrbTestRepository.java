package com.clinical.repository.irb;

import com.clinical.entity.irb.IrbTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrbTestRepository extends JpaRepository<IrbTestEntity, Long> {

    /**
     * 모든 게시글을 계층형 구조(원글 -> 답글)로 정렬하여 전체 조회
     * 원글 A (ID 10) -> 답글 (ID 12) -> 원글 B (ID 20) 순서로 나옴
     */
    @Query(value =
            "WITH RECURSIVE irb_hierarchy AS (" +
                    "    SELECT *, CAST(id AS CHAR(200)) AS path " +
                    "    FROM irb_test " +
                    "    WHERE (depth = 0 OR depth IS NULL) " +
                    "    UNION ALL " +
                    "    SELECT t.*, CONCAT(h.path, ',', t.id) " +
                    "    FROM irb_test t " +
                    "    INNER JOIN irb_hierarchy h ON t.irb_test_id_ref = h.id " +
                    ") " +
                    "SELECT * FROM irb_hierarchy ORDER BY path",
            nativeQuery = true)
    List<IrbTestEntity> findAllHierarchy();
    /**
     * 임시저장(is_temp=1)을 제외한 모든 '운영 중'인 게시글만 계층형 조회
     */
    @Query(value =
            "WITH RECURSIVE irb_hierarchy AS (" +
                    "    SELECT *, CAST(id AS CHAR(200)) AS path " +
                    "    FROM irb_test " +
                    "    WHERE (depth = 0 OR depth IS NULL) AND (is_temp = 0 OR is_temp IS NULL) " +
                    "    UNION ALL " +
                    "    SELECT t.*, CONCAT(h.path, ',', t.id) " +
                    "    FROM irb_test t " +
                    "    INNER JOIN irb_hierarchy h ON t.irb_test_id_ref = h.id " +
                    "    WHERE (t.is_temp = 0 OR t.is_temp IS NULL) " +
                    ") " +
                    "SELECT * FROM irb_hierarchy ORDER BY path",
            nativeQuery = true)
    List<IrbTestEntity> findAllActiveHierarchy();

    long countByStatusAndIsTemp(String status, Integer isTemp);

    List<IrbTestEntity> findTop5ByIsTempAndDepthOrderByCreatedAtDesc(Integer isTemp, Integer depth);
}