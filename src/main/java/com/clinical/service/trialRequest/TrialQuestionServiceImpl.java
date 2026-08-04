package com.clinical.service.trialRequest;

import com.clinical.dto.trialRequest.TrialQuestionRequestDTO;
import com.clinical.dto.trialRequest.TrialQuestionResponseDTO;
import com.clinical.entity.trialRequest.TrialAnswer;
import com.clinical.entity.trialRequest.TrialQuestion;
import com.clinical.entity.user.UserEntity;
import com.clinical.repository.trialRequest.TrialAnswerRepository;
import com.clinical.repository.trialRequest.TrialQuestionRepository;
import com.clinical.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrialQuestionServiceImpl implements TrialQuestionService {

    private final TrialQuestionRepository trialQuestionRepository;
    private final TrialAnswerRepository trialAnswerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TrialQuestionResponseDTO.DetailResponse createQuestion(TrialQuestionRequestDTO.Create request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        TrialQuestion question = TrialQuestion.builder()
                .title(request.getTitle())
                .companyName(request.getCompanyName())
                .companyId(request.getCompanyId())
                .representativeName(request.getRepresentativeName())
                .representativePosition(request.getRepresentativePosition())
                .phone(request.getPhone())
                .email(request.getEmail())
                .productType(request.getProductType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .password(encodedPassword)
                .contactType(request.getContactType())
                .content(request.getContent())
                .status("PENDING")
                .build();

        TrialQuestion saved = trialQuestionRepository.save(question);
        return toDetailResponse(saved);
    }

    @Override
    public List<TrialQuestionResponseDTO.ListResponse> getAllQuestions() {
        List<TrialQuestion> questions = trialQuestionRepository.findAllByOrderByCreatedAtDesc();

        List<TrialQuestionResponseDTO.ListResponse> result = new ArrayList<>();

        for (TrialQuestion question : questions) {
            result.add(toListResponse(question, false, null));

            List<TrialAnswer> answers = trialAnswerRepository.findByQuestionIdOrderByCreatedAtDesc(question.getId());

            for (TrialAnswer answer : answers) {
                result.add(toAnswerListResponse(answer, question.getId()));
            }
        }

        return result;
    }

    @Override
    public List<TrialQuestionResponseDTO.ListResponse> getQuestionsByPhone(String phone) {
        List<TrialQuestion> questions = trialQuestionRepository.findByPhoneOrderByCreatedAtDesc(phone);

        List<TrialQuestionResponseDTO.ListResponse> result = new ArrayList<>();

        for (TrialQuestion question : questions) {
            result.add(toListResponse(question, false, null));

            List<TrialAnswer> answers = trialAnswerRepository.findByQuestionIdOrderByCreatedAtDesc(question.getId());
            for (TrialAnswer answer : answers) {
                result.add(toAnswerListResponse(answer, question.getId()));
            }
        }

        return result;
    }

    @Override
    public boolean verifyPassword(Long id, String password) {
        TrialQuestion question = trialQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        return passwordEncoder.matches(password, question.getPassword());
    }

    @Override
    public TrialQuestionResponseDTO.DetailResponse getQuestionById(Long id) {
        TrialQuestion question = trialQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        return toDetailResponse(question);
    }

    @Override
    @Transactional
    public TrialQuestionResponseDTO.DetailResponse updateQuestion(Long id, TrialQuestionRequestDTO.Update request) {
        TrialQuestion question = trialQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        if (request.getPassword() != null && !passwordEncoder.matches(request.getPassword(), question.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        question.setTitle(request.getTitle());
        question.setCompanyName(request.getCompanyName());
        question.setCompanyId(request.getCompanyId());
        question.setRepresentativeName(request.getRepresentativeName());
        question.setRepresentativePosition(request.getRepresentativePosition());
        question.setPhone(request.getPhone());
        question.setEmail(request.getEmail());
        question.setProductType(request.getProductType());
        question.setStartDate(request.getStartDate());
        question.setEndDate(request.getEndDate());
        question.setContactType(request.getContactType());
        question.setContent(request.getContent());

        return toDetailResponse(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id, String password) {
        TrialQuestion question = trialQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, question.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        trialAnswerRepository.deleteByQuestionId(id);

        trialQuestionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TrialQuestionResponseDTO.DetailResponse createAnswer(Long questionId, TrialQuestionRequestDTO.CreateAnswer request) {
        TrialQuestion question = trialQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        TrialAnswer answer = TrialAnswer.builder()
                .questionId(questionId)
                .userId(request.getUserId())
                .content(request.getContent())
                .build();

        trialAnswerRepository.save(answer);

        question.setStatus("ANSWERED");

        return toDetailResponse(question);
    }

    @Override
    public List<TrialQuestionResponseDTO.DetailResponse> getAllQuestionsForAdmin() {
        List<TrialQuestion> questions = trialQuestionRepository.findAllByOrderByCreatedAtDesc();
        return questions.stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void adminDeleteQuestion(Long id) {
        if (!trialQuestionRepository.existsById(id)) {
            throw new IllegalArgumentException("문의글을 찾을 수 없습니다.");
        }
        trialAnswerRepository.deleteByQuestionId(id);
        trialQuestionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TrialQuestionResponseDTO.DetailResponse updateAnswer(Long questionId, Long answerId, String content) {
        TrialQuestion question = trialQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        TrialAnswer answer = trialAnswerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        answer.setContent(content);
        trialAnswerRepository.save(answer);
        return toDetailResponse(question);
    }

    @Override
    @Transactional
    public void deleteAnswer(Long questionId, Long answerId) {
        TrialQuestion question = trialQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));

        trialAnswerRepository.deleteById(answerId);

        if (!trialAnswerRepository.existsByQuestionId(questionId)) {
            question.setStatus("PENDING");
        }
    }

    private TrialQuestionResponseDTO.ListResponse toListResponse(TrialQuestion entity, boolean isAnswer, Long questionId) {
        String maskedName = maskName(entity.getRepresentativeName());

        boolean hasAnswer = trialAnswerRepository.existsByQuestionId(entity.getId());

        return TrialQuestionResponseDTO.ListResponse.builder()
                .id(entity.getId())
                .title("비밀글입니다")
                .representativeName(maskedName)
                .status(entity.getStatus())
                .hasAnswer(hasAnswer)
                .createdAt(entity.getCreatedAt())
                .isProtected(true)
                .isAnswer(isAnswer)
                .questionId(questionId)
                .build();
    }

    private TrialQuestionResponseDTO.ListResponse toAnswerListResponse(TrialAnswer answer, Long questionId) {
        return TrialQuestionResponseDTO.ListResponse.builder()
                .id(answer.getId())
                .title("Re: 비밀글입니다")
                .representativeName("관리자")
                .status("ANSWERED")
                .hasAnswer(false)
                .createdAt(answer.getCreatedAt())
                .isProtected(true)
                .isAnswer(true)
                .questionId(questionId)
                .build();
    }

    private TrialQuestionResponseDTO.DetailResponse toDetailResponse(TrialQuestion entity) {
        List<TrialAnswer> answers = trialAnswerRepository.findByQuestionIdOrderByCreatedAtDesc(entity.getId());

        List<TrialQuestionResponseDTO.AnswerResponse> answerResponses = answers.stream()
                .map(this::toAnswerResponse)
                .collect(Collectors.toList());

        return TrialQuestionResponseDTO.DetailResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .companyName(entity.getCompanyName())
                .companyId(entity.getCompanyId())
                .representativeName(entity.getRepresentativeName())
                .representativePosition(entity.getRepresentativePosition())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .productType(entity.getProductType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .contactType(entity.getContactType())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .hasAnswer(!answers.isEmpty())
                .answers(answerResponses)
                .build();
    }

    private TrialQuestionResponseDTO.AnswerResponse toAnswerResponse(TrialAnswer answer) {
        String userName = "관리자";
        if (answer.getUserId() != null) {
            UserEntity user = userRepository.findById(answer.getUserId()).orElse(null);
            if (user != null) {
                userName = "ADMIN".equals(user.getPosition()) ? "관리자" : user.getName();
            }
        }

        return TrialQuestionResponseDTO.AnswerResponse.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .userId(answer.getUserId())
                .userName(userName)
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }

    private String maskName(String name) {
        if (name == null || name.length() == 0) {
            return "익명";
        }

        if (name.length() == 1) {
            return name + "*";
        }

        return name.charAt(0) + "**";
    }
}