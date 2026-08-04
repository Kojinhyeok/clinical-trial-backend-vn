package com.clinical.service.irb;

import com.clinical.dto.irb.IrbSurveyResultDTO;
import com.clinical.entity.irb.IrbSurveyResultEntity;
import com.clinical.mapper.irb.IrbSurveyResultMapper;
import com.clinical.repository.irb.IrbSurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IrbSurveyResultService {

    private final IrbSurveyResultRepository resultRepository;
    private final IrbSurveyResultMapper resultMapper;

    /**
     * 심사 결과 등록 또는 수정 (Upsert)
     */
    @Transactional
    public IrbSurveyResultDTO submitReviewResult(IrbSurveyResultDTO dto) {
        // 1. 기존에 이 심사자가 해당 IRB에 대해 내린 결과가 있는지 확인
        IrbSurveyResultEntity entity = resultRepository.findByIrbTestIdAndUserId(dto.getIrbTestId(), dto.getUserId())
                .map(existing -> {
                    // 2. 있다면 내용 수정 (Dirty Checking)
                    existing.setReviewResult(dto.getReviewResult());
                    existing.setReviewText(dto.getReviewText());
                    existing.setSurveyTemplateId(dto.getSurveyTemplateId());
                    return existing;
                })
                .orElseGet(() -> {
                    // 3. 없다면 새로 생성
                    return resultMapper.toEntity(dto);
                });

        // 4. 저장 및 결과 반환
        IrbSurveyResultEntity saved = resultRepository.save(entity);
        return resultMapper.toDto(saved);
    }
    /**
     * IRB 시험의 전체 심사 결과 리스트 조회 (관리자용)
     */
    public List<IrbSurveyResultDTO> getAllResult() {
        return resultRepository.findAll().stream().map(resultMapper::toDto).toList();
    }
    /**
     * 특정 IRB 시험의 심사 결과 조회
     */
    public List<IrbSurveyResultDTO> getResultByTestId(Long irbTestId) {
        return resultRepository.findAllByIrbTestId(irbTestId).stream()
                .map(resultMapper::toDto).toList();
    }

    /**
     * 특정 IRB 시험의 심사 결과 조회 (단건 - 내 심사 결과)
     */
    public IrbSurveyResultDTO getMyResult(Long irbTestId, Long userId) {
        return resultRepository.findByIrbTestIdAndUserId(irbTestId, userId)
                .map(resultMapper::toDto)
                .orElse(null); // 혹은 예외 처리
    }

    /**
     * 특정 IRB 시험의 전체 심사 결과 리스트 조회 (관리자용)
     */
    public List<IrbSurveyResultDTO> getAllResultsByTestId(Long irbTestId) {
        return resultRepository.findAllByIrbTestId(irbTestId).stream()
                .map(resultMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 심사결과 삭제
     * */
    @Transactional
    public void delete(Long id){
        resultRepository.findById(id).orElseThrow(()->new RuntimeException("삭제할 심사결과가 없습니다."));
        resultRepository.deleteById(id);
    }
}