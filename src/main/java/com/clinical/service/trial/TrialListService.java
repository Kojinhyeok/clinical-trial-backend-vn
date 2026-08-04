package com.clinical.service.trial;

import com.clinical.dto.trial.TrialListRequestDTO;
import com.clinical.dto.trial.TrialListResponseDTO;

import java.util.List;

/**
 * TrialList Service Interface
 */
public interface TrialListService {

    /**
     * 시험 생성
     */
    TrialListResponseDTO.DetailResponse createTrial(TrialListRequestDTO.Create request);

    /**
     * 시험 수정
     */
    TrialListResponseDTO.DetailResponse updateTrial(Long id, TrialListRequestDTO.Update request);

    /**
     * 시험 삭제
     */
    void deleteTrial(Long id);

    /**
     * 시험 상세 조회 (조회수 증가 + 전/후 이미지 URL 포함)
     */
    TrialListResponseDTO.DetailResponse getTrialById(Long id);

    /**
     * 전체 시험 목록 조회 (최신순)
     */
    List<TrialListResponseDTO.ListResponse> getAllTrials();

    /**
     * 키워드 그룹별 시험 조회
     */
    List<TrialListResponseDTO.ListResponse> getTrialsByGroup(Long keywordGroupId);

    /**
     * 키워드 검색 (탭 내에서 필터링)
     * @param keyword 검색 키워드
     * @param tabCode 현재 선택된 탭 코드 (BASIC, MAKEUP 등) - 선택사항
     */
    List<TrialListResponseDTO.ListResponse> searchTrialsByKeyword(String keyword, String tabCode);
}