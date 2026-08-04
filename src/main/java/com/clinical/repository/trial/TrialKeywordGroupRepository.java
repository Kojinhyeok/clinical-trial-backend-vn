package com.clinical.repository.trial;

import com.clinical.entity.trial.TrialKeywordGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 시험 키워드 그룹 Repository
 */
@Repository
public interface TrialKeywordGroupRepository extends JpaRepository<TrialKeywordGroup, Long> {

    /**
     * 탭 코드로 키워드 그룹 조회
     * @param tabCode BASIC, MAKEUP, CLEANSER, SCALP_HAIR, ETC
     * @return 해당 탭의 키워드 그룹 목록 (정렬 순서대로)
     */
    List<TrialKeywordGroup> findByTabCodeOrderByDisplayOrderAsc(String tabCode);

    /**
     * 모든 키워드 그룹 조회 (정렬 순서대로)
     * @return 전체 키워드 그룹 목록
     */
    List<TrialKeywordGroup> findAllByOrderByDisplayOrderAsc();

    /**
     * 탭 코드 존재 여부 확인
     * @param tabCode 탭 코드
     * @return 존재 여부
     */
    boolean existsByTabCode(String tabCode);

    /**
     * 탭 코드로 키워드 그룹 조회 (정렬 없음)
     * - 검색 필터링용
     * @param tabCode 탭 코드
     * @return 해당 탭의 키워드 그룹 목록
     */
    List<TrialKeywordGroup> findByTabCode(String tabCode);

    /**
     * 그룹명으로 조회
     * @param groupName 그룹명
     * @return 키워드 그룹
     */
    List<TrialKeywordGroup> findByGroupNameContaining(String groupName);

    /**
     * 탭별 추천 검색어 조회 (검색창 드롭다운용)
     * - UI: 검색창 클릭 시 표시되는 추천 검색어 목록
     * @param tabCode 탭 코드
     * @return 추천 검색어(group_name) 목록
     */
    @Query("SELECT tkg.groupName FROM TrialKeywordGroup tkg WHERE tkg.tabCode = :tabCode ORDER BY tkg.displayOrder")
    List<String> findGroupNamesByTabCode(@Param("tabCode") String tabCode);

    /**
     * 전체 추천 검색어 조회 (모든 탭)
     * @return 추천 검색어 목록
     */
    @Query("SELECT tkg.groupName FROM TrialKeywordGroup tkg ORDER BY tkg.displayOrder")
    List<String> findAllGroupNames();
}