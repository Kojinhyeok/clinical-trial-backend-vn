package com.clinical.repository.trial;

import com.clinical.entity.trial.TrialList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 시험 목록 Repository
 * ⚠️ 페이징과 검색 필터는 프론트엔드에서 처리
 */
@Repository
public interface TrialListRepository extends JpaRepository<TrialList, Long> {

    /**
     * 전체 시험 목록 조회 (최신순)
     * @return 시험 목록
     */
    List<TrialList> findAllByOrderByCreatedAtDesc();

    /**
     * 키워드 그룹 ID로 시험 목록 조회
     * @param keywordGroupId 키워드 그룹 ID
     * @return 시험 목록
     */
    List<TrialList> findByKeywordGroupId(Long keywordGroupId);

    /**
     * 여러 키워드 그룹 ID로 시험 목록 조회
     * - 키워드 검색 시 여러 그룹에 걸쳐 검색
     * @param keywordGroupIds 키워드 그룹 ID 목록
     * @return 시험 목록
     */
    List<TrialList> findByKeywordGroupIdIn(List<Long> keywordGroupIds);

    /**
     * 키워드 그룹 ID 존재 여부 확인
     * @param keywordGroupId 키워드 그룹 ID
     * @return 존재 여부
     */
    boolean existsByKeywordGroupId(Long keywordGroupId);

    /**
     * 키워드 그룹 ID로 시험 개수 조회
     * @param keywordGroupId 키워드 그룹 ID
     * @return 시험 개수
     */
    long countByKeywordGroupId(Long keywordGroupId);
}