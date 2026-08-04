package com.clinical.service.trialRequest;

import com.clinical.dto.trialRequest.TrialQuestionRequestDTO;
import com.clinical.dto.trialRequest.TrialQuestionResponseDTO;

import java.util.List;

public interface TrialQuestionService {

    TrialQuestionResponseDTO.DetailResponse createQuestion(TrialQuestionRequestDTO.Create request);

    List<TrialQuestionResponseDTO.ListResponse> getAllQuestions();

    List<TrialQuestionResponseDTO.ListResponse> getQuestionsByPhone(String phone);

    boolean verifyPassword(Long id, String password);

    TrialQuestionResponseDTO.DetailResponse getQuestionById(Long id);

    TrialQuestionResponseDTO.DetailResponse updateQuestion(Long id, TrialQuestionRequestDTO.Update request);

    void deleteQuestion(Long id, String password);

    TrialQuestionResponseDTO.DetailResponse createAnswer(Long questionId, TrialQuestionRequestDTO.CreateAnswer request);

    List<TrialQuestionResponseDTO.DetailResponse> getAllQuestionsForAdmin();

    void adminDeleteQuestion(Long id);

    TrialQuestionResponseDTO.DetailResponse updateAnswer(Long questionId, Long answerId, String content);

    void deleteAnswer(Long questionId, Long answerId);
}