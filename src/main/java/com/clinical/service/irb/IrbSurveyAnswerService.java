package com.clinical.service.irb;

import com.clinical.dto.irb.IrbSurveyAnswerDTO;
import com.clinical.entity.irb.IrbSurveyAnswerEntity;
import com.clinical.mapper.irb.IrbSurveyAnswerMapper;
import com.clinical.repository.irb.IrbSurveyAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IrbSurveyAnswerService {

    private final IrbSurveyAnswerRepository irbSurveyAnswerRepository;
    private final IrbSurveyAnswerMapper irbSurveyAnswerMapper;

    /**
     * 답변 저장 또는 수정
     */
    @Transactional
    public IrbSurveyAnswerDTO submitAnswer(IrbSurveyAnswerDTO dto) {
        // 1. 기존 답변이 있는지 확인
        IrbSurveyAnswerEntity entity = irbSurveyAnswerRepository
                .findByIrbTestIdAndUserId(dto.getIrbTestId(), dto.getUserId())
                .map(existing -> {
                    // 2. 존재하면 답변 내용(JSON)과 템플릿 ID 업데이트 (Dirty Checking)
                    existing.setAnswerList(dto.getAnswerList());
                    existing.setSurveyTemplateId(dto.getSurveyTemplateId());
                    return existing;
                })
                .orElseGet(() -> {
                    // 3. 존재하지 않으면 새로 생성
                    return irbSurveyAnswerRepository.save(irbSurveyAnswerMapper.toEntity(dto));
                });

        return irbSurveyAnswerMapper.toDto(entity);
    }

    /**
     * 답변 단건 조회
     */
    public IrbSurveyAnswerDTO getAnswer(Long irbTestId, Long userId) {
        return irbSurveyAnswerRepository.findByIrbTestIdAndUserId(irbTestId, userId)
                .map(irbSurveyAnswerMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("작성된 답변을 찾을 수 없습니다."));
    }
    //testId로 답변 조회
    public List<IrbSurveyAnswerDTO> getAnswersByTestId(Long irbTestId){
        return irbSurveyAnswerRepository.findByIrbTestId(irbTestId).stream().map(irbSurveyAnswerMapper::toDto).toList();
    }
    //답변 삭제
    @Transactional
    public void delete(Long id){
        irbSurveyAnswerRepository.findById(id).orElseThrow(()->new RuntimeException("찾을수 없는 답변입니다."));
        irbSurveyAnswerRepository.deleteById(id);
    }
}