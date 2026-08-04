package com.clinical.service.irb;

import com.clinical.dto.irb.IrbSurveyAnswerDTO;
import com.clinical.dto.irb.IrbSurveyRequestDTO;
import com.clinical.dto.irb.IrbSurveyResponseDTO;
import com.clinical.dto.irb.IrbSurveyResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IrbSurveyService {
    private final IrbSurveyResultService resultService;
    private final IrbSurveyAnswerService answerService;
    private final IrbTestService testService;

    //심사 작성
    public void survey(IrbSurveyRequestDTO requestDTO){
        answerService.submitAnswer(requestDTO.getAnswerDTO());
        resultService.submitReviewResult(requestDTO.getResultDTO());
    }
    //심사 전체 조회
    public List<IrbSurveyResponseDTO> findAll() {
        // 1. 모든 테스트(원글) 조회
        return testService.findAll().stream()
                .flatMap(test -> {
                    // 2. 해당 테스트에 달린 모든 답변 조회
                    List<IrbSurveyAnswerDTO> answers = answerService.getAnswersByTestId(test.getId());

                    // 3. 답변이 하나도 없다면 빈 껍데기라도 보낼지, 제외할지 결정 (여기서는 답변 기준으로 생성)
                    return answers.stream().map(answer -> {
                        // 4. 답변 작성자(UserId)에 매칭되는 결과 조회
                        IrbSurveyResultDTO result = resultService.getMyResult(test.getId(), answer.getUserId());

                        // 5. 기존 필드를 유지하며 DTO 조립
                        IrbSurveyResponseDTO response = new IrbSurveyResponseDTO();
                        response.setTestResponseDTO(test);      // 원글 정보 (공통)
                        response.setAnswerDTO(answer);          // 개별 답변
                        response.setResultDTO(result);          // 개별 결과

                        return response;
                    });
                })
                .collect(Collectors.toList());
    }
    //심사 수정
    public void surveyUpdate(Long testId,Long userId,IrbSurveyRequestDTO requestDTO){
        IrbSurveyAnswerDTO find =answerService.getAnswer(testId,userId);
        find.setSurveyTemplateId(requestDTO.getAnswerDTO().getSurveyTemplateId());
        find.setAnswerList(requestDTO.getAnswerDTO().getAnswerList());
        IrbSurveyResultDTO findResult = resultService.getMyResult(testId,userId);
        findResult.setSurveyTemplateId(requestDTO.getAnswerDTO().getSurveyTemplateId());
        findResult.setReviewResult(requestDTO.getResultDTO().getReviewResult());
        findResult.setReviewText(requestDTO.getResultDTO().getReviewText());
        answerService.submitAnswer(find);
        resultService.submitReviewResult(requestDTO.getResultDTO());
    }
    //심사 삭제
    public void delete(Long testId,Long userId){
        IrbSurveyAnswerDTO find =answerService.getAnswer(testId,userId);
        answerService.delete(find.getId());
        IrbSurveyResultDTO findResult = resultService.getMyResult(testId,userId);
        resultService.delete(findResult.getId());
    }


}
