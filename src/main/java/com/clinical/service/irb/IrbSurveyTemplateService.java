package com.clinical.service.irb;

import com.clinical.dto.irb.IrbSurveyTemplateDTO;
import com.clinical.entity.irb.IrbSurveyTemplateEntity;
import com.clinical.mapper.irb.IrbSurveyTemplateMapper;
import com.clinical.repository.irb.IrbSurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IrbSurveyTemplateService {

    private final IrbSurveyTemplateRepository templateRepository;
    private final IrbSurveyTemplateMapper templateMapper;

    /**
     * 심사 템플릿 등록
     */
    @Transactional
    public IrbSurveyTemplateDTO createTemplate(IrbSurveyTemplateDTO dto) {
        IrbSurveyTemplateEntity entity = templateMapper.toEntity(dto);
        return templateMapper.toDto(templateRepository.save(entity));
    }

    /**
     * 특정 IRB 시험의 특정 템플릿 조회 (심사 시작 시 문항 로딩용)
     */
    public IrbSurveyTemplateDTO getTemplate(Long irbTestId, String surveyName) {
        return templateRepository.findByIrbTestIdAndSurveyName(irbTestId, surveyName)
                .or(() -> templateRepository.findByIrbTestIdAndSurveyName(null, surveyName)) 
                .map(templateMapper::toDto)
                .orElseThrow(() -> new RuntimeException("해당 IRB 심사 템플릿을 찾을 수 없습니다."));
    }
    /**
     * 특정 IRB 시험의 특정 템플릿 조회 (심사 시작 시 문항 로딩용)
     */
    public List<IrbSurveyTemplateDTO> getTemplatesAll() {
        return templateRepository.findAll().stream().map(templateMapper::toDto).toList();
    }

    /**
     * 템플릿 수정 (문항 업데이트)
     */
    @Transactional
    public IrbSurveyTemplateDTO updateTemplate(Long id, IrbSurveyTemplateDTO dto) {
        IrbSurveyTemplateEntity template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 템플릿이 존재하지 않습니다."));

        template.setSurveyName(dto.getSurveyName());
        template.setQuestionList(dto.getQuestionList()); // Dirty Checking으로 JSON 업데이트

        return templateMapper.toDto(template);
    }
    /**
     * 템플릿 삭제
     */
    public void delete(Long id){
        IrbSurveyTemplateEntity template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제할 템플릿이 존재하지 않습니다."));
        templateRepository.deleteById(template.getId());
    }

}