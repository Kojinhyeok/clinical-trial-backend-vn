package com.clinical.repository.trial;

import com.clinical.entity.trial.TrialSearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 시험 검색 키워드 Repository
 */
@Repository
public interface TrialSearchKeywordRepository extends JpaRepository<TrialSearchKeyword, Long> {

    /**
     * 키워드 그룹 ID로 검색 키워드 조회
     * @param keywordGroupId 키워드 그룹 ID
     * @return 해당 그룹의 검색 키워드 목록
     */
    List<TrialSearchKeyword> findByKeywordGroupId(Long keywordGroupId);

    /**
     * 키워드로 검색
     * @param keyword 검색할 키워드
     * @return 해당 키워드를 포함하는 검색 키워드 목록
     */
    List<TrialSearchKeyword> findByKeywordContaining(String keyword);

    /**
     * 정확히 일치하는 키워드 조회
     * @param keyword 검색 키워드
     * @return 검색 키워드 목록
     */
    List<TrialSearchKeyword> findByKeyword(String keyword);

    /**
     * 여러 키워드로 검색
     * @param keywords 검색 키워드 목록
     * @return 검색 키워드 목록
     */
    List<TrialSearchKeyword> findByKeywordIn(List<String> keywords);

    /**
     * 키워드로 키워드 그룹 ID 조회
     * - 사용자 입력에 DB 키워드가 포함되어 있으면 매칭
     * - 예: 입력 "샴푸로 머리를 감아요" → DB 키워드 "샴푸"가 포함됨 → 매칭
     * @param keyword 사용자 검색 입력
     * @return 키워드 그룹 ID 목록
     */
    @Query("SELECT DISTINCT tsk.keywordGroupId FROM TrialSearchKeyword tsk WHERE :keyword LIKE CONCAT('%', tsk.keyword, '%')")
    List<Long> findKeywordGroupIdsByKeyword(@Param("keyword") String keyword);

    /**
     * 키워드 존재 여부 확인
     * @param keywordGroupId 키워드 그룹 ID
     * @param keyword 키워드
     * @return 존재 여부
     */
    boolean existsByKeywordGroupIdAndKeyword(Long keywordGroupId, String keyword);

    /**
     * 키워드 그룹 ID로 모든 키워드 삭제
     * @param keywordGroupId 키워드 그룹 ID
     */
    void deleteByKeywordGroupId(Long keywordGroupId);
}