package com.clinical.service.irb;

import com.clinical.dto.irb.IrbSurveyQuestionDTO;
import com.clinical.entity.irb.IrbSurveyQuestionEntity;
import com.clinical.mapper.irb.IrbSurveyQuestionMapper;
import com.clinical.repository.irb.IrbSurveyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IrbSurveyQuestionService {

    private final IrbSurveyQuestionRepository questionRepository;
    private final IrbSurveyQuestionMapper questionMapper;

    /**
     * 카테고리별 활성 문항 리스트 조회 (설문지 생성용)
     */
    public List<IrbSurveyQuestionDTO> getAll(){
        return questionRepository.findAll().stream().map(questionMapper::toDto).toList();
    }

    /**
     * 카테고리별 활성 문항 리스트 조회 (설문지 생성용)
     */
    public List<IrbSurveyQuestionDTO> getQuestionsByCategory(String category) {
        return questionRepository.findAllByQuestionCategoryAndIsActiveOrderByQuestionOrderAsc(category, 1)
                .stream()
                .map(questionMapper::toDto)
                .toList();
    }


    /**
     * 신규 문항 등록
     */
    @Transactional
    public IrbSurveyQuestionDTO createQuestion(IrbSurveyQuestionDTO dto) {
        IrbSurveyQuestionEntity entity = questionMapper.toEntity(dto);
        return questionMapper.toDto(questionRepository.save(entity));
    }

    /**
     * 문항 수정 (Dirty Checking)
     */
    @Transactional
    public IrbSurveyQuestionDTO updateQuestion(Long id, IrbSurveyQuestionDTO dto) {
        IrbSurveyQuestionEntity entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문항을 찾을 수 없습니다. id=" + id));

        entity.setQuestionText(dto.getQuestionText());
        entity.setQuestionCategory(dto.getQuestionCategory());
        entity.setQuestionType(dto.getQuestionType());
        entity.setQuestionOrder(dto.getQuestionOrder());
        entity.setIsActive(dto.getIsActive());

        return questionMapper.toDto(entity);
    }
    //문항 삭제
    public void deleteQuestion(Long id){
        questionRepository.findById(id).orElseThrow(()->new RuntimeException("문항을 찾을 수 없습니다."));
        questionRepository.deleteById(id);
    }
}