package com.clinical.service.trial;

import com.clinical.dto.trial.TrialSearchKeywordRequestDTO;
import com.clinical.dto.trial.TrialSearchKeywordResponseDTO;

import java.util.List;

/**
 * 시험 검색 키워드 Service
 */
public interface TrialSearchKeywordService {

    /**
     * 키워드 생성
     * @param request 생성 요청 DTO
     * @return 생성된 키워드
     */
    TrialSearchKeywordResponseDTO.DetailResponse createKeyword(TrialSearchKeywordRequestDTO.Create request);

    /**
     * 키워드 일괄 생성
     * @param request 일괄 생성 요청 DTO
     * @return 생성된 키워드 목록
     */
    List<TrialSearchKeywordResponseDTO.SimpleResponse> bulkCreateKeywords(TrialSearchKeywordRequestDTO.BulkCreate request);

    /**
     * 키워드 수정
     * @param id 키워드 ID
     * @param request 수정 요청 DTO
     * @return 수정된 키워드
     */
    TrialSearchKeywordResponseDTO.DetailResponse updateKeyword(Long id, TrialSearchKeywordRequestDTO.Update request);

    /**
     * 키워드 삭제
     * @param id 키워드 ID
     */
    void deleteKeyword(Long id);

    /**
     * 키워드 상세 조회
     * @param id 키워드 ID
     * @return 키워드 상세
     */
    TrialSearchKeywordResponseDTO.DetailResponse getKeywordById(Long id);

    /**
     * 전체 키워드 조회
     * @return 키워드 목록
     */
    List<TrialSearchKeywordResponseDTO.ListResponse> getAllKeywords();

    /**
     * 키워드 그룹별 조회
     * @param keywordGroupId 키워드 그룹 ID
     * @return 키워드 목록
     */
    List<TrialSearchKeywordResponseDTO.ListResponse> getKeywordsByGroup(Long keywordGroupId);

    /**
     * 키워드로 그룹 ID 검색
     * @param keyword 검색 키워드
     * @return 키워드 그룹 ID 목록
     */
    List<Long> findGroupIdsByKeyword(String keyword);
}