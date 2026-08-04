package com.clinical.service.irb;

import com.clinical.dto.file.FileResponseDTO;
import com.clinical.dto.irb.IrbTestRequestDTO;
import com.clinical.dto.irb.IrbTestResponseDTO;
import com.clinical.entity.enumuration.EmailType;
import com.clinical.entity.enumuration.EntityCategory;
import com.clinical.entity.enumuration.EntityType;
import com.clinical.entity.enumuration.IrbStatus;
import com.clinical.entity.irb.IrbEmailListEntity;
import com.clinical.entity.irb.IrbTestEntity;
import com.clinical.mapper.irb.IrbTestMapper;
import com.clinical.repository.irb.IrbTestRepository;
import com.clinical.service.EmailService;
import com.clinical.service.email.EmailTemplateService;
import com.clinical.repository.irb.IrbEmailListRepository;
import com.clinical.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinical.entity.irb.IrbSurveyResultEntity;
import com.clinical.repository.irb.IrbSurveyResultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;
import com.clinical.dto.irb.UserEmailDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class IrbTestService {

    private final IrbTestRepository irbTestRepository;
    private final IrbEmailListRepository irbEmailListRepository;
    private final IrbSurveyResultRepository irbSurveyResultRepository;
    private final IrbTestMapper irbMapper;
    private final FileService fileService;
    private final IrbEmailListService emailListService;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    @Value("${email.base-url}")
    private String baseUrl;

    //임시저장 포함 irb 전체 조회
    public List<IrbTestResponseDTO> findAll(){
        List<IrbTestResponseDTO> dtos = irbTestRepository.findAllHierarchy().stream()
                .map(irb -> irbMapper.toDto(irb, null))
                .collect(Collectors.toList());

        // 심사 결과를 한 번에 조회 후 irbTestId 기준 그룹핑 (null 제외)
        Map<Long, List<IrbSurveyResultEntity>> resultsByTestId =
                irbSurveyResultRepository.findAll().stream()
                        .filter(r -> r.getIrbTestId() != null)
                        .collect(Collectors.groupingBy(IrbSurveyResultEntity::getIrbTestId));

        dtos.forEach(dto -> {
            List<IrbSurveyResultEntity> results = resultsByTestId.get(dto.getId());
            if (results != null && !results.isEmpty()) {
                boolean allApproved = results.stream()
                        .allMatch(r -> "APPROVED".equals(r.getReviewResult()));
                dto.setReviewStatus(allApproved ? "ALL_APPROVED" : "REVIEW_NEEDED");
            }
        });

        return dtos;
    }
    
    //임시저장 제외 irb 전체 조회 (권한 체크 없음 - 모두에게 목록 공개)
    public List<IrbTestResponseDTO> findIsNotTempAll(){
        return irbTestRepository.findAllActiveHierarchy().stream()
                .map(irb -> irbMapper.toDto(irb, null))
                .toList();
    }

    //irb 상세 조회 (권한 체크 - 메일 받은 사람만)
    public IrbTestResponseDTO findDetail(Long id, String userEmail, boolean isAdmin){
        IrbTestEntity irb = irbTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 irb를 찾을 수 없습니다. ID: " + id));
        
        // 관리자가 아니면 권한 체크
        if (!isAdmin) {
            List<Long> accessibleIrbIds = irbEmailListRepository.findIrbTestIdsByEmail(userEmail);
            if (!accessibleIrbIds.contains(irb.getIrbTestId())) {
                throw new RuntimeException("접근 권한이 없습니다. 메일 수신자만 조회할 수 있습니다.");
            }
        }

        // 해당 irb에 묶인 파일 목록 조회
        List<FileResponseDTO> files = fileService.getFilesByEntity(EntityType.IRB, id);
        return irbMapper.toDto(irb, files);
    }


    //irb 원글 작성
    @Transactional
    public IrbTestResponseDTO write(IrbTestRequestDTO requestDTO){
        requestDTO.setStatus(IrbStatus.IN_REVIEW);
        //원글 저장 후 irb_test_id 컬럼 저장을 위한 세팅
        IrbTestEntity saved = irbTestRepository.save(irbMapper.toEntity(requestDTO));
        saved.setIrbTestId(saved.getId());
        //이메일 리스트 저장 및 이메일 발송
        emailListService.saveEmailLog(saved.getId(), requestDTO.getEmails(), EmailType.NEW_POST);
        String irbUrl = baseUrl + "/irb/irb_detail.html?id=" + saved.getId();
        String emailHtml = emailTemplateService.buildIrbNotificationEmail(saved.getTitle(), irbUrl);
        emailService.sendEmails(requestDTO.getEmails(), "[휴먼피부임상시험센터] IRB 심사참여 요청 - " + saved.getTitle(), emailHtml);
        //파일 저장
        List<FileResponseDTO> fileResponses = null;
        if (requestDTO.getFiles() != null && !requestDTO.getFiles().isEmpty()) {
            fileResponses = fileService.getMultipleUploadLinks(
                    saved.getId(),
                    EntityType.IRB,
                    EntityCategory.ATTACHMENT,
                    requestDTO.getUserId(),
                    requestDTO.getFiles());
        }
        return irbMapper.toDto(irbTestRepository.save(saved),fileResponses);
    }
    
    //irb 답글 작성
    @Transactional
    public IrbTestResponseDTO createReply(Long parentId, IrbTestRequestDTO reply) {
        IrbTestEntity parent = irbTestRepository.findById(parentId).orElseThrow();

        //답글 저장
        reply.setIrbTestId(parent.getIrbTestId()); // 원글 ID는 부모와 동일하게
        reply.setIrbTestIdRef(parent.getId());    // 직전 부모 ID 세팅
        reply.setDepth(parent.getDepth() + 1);     // 부모 depth + 1
        IrbTestEntity replyEntity = irbMapper.toEntity(reply);
        IrbTestEntity saved = irbTestRepository.save(replyEntity);

        //이메일 리스트 저장 및 이메일 발송
        emailListService.saveEmailLog(saved.getId(), reply.getEmails(), EmailType.NEW_ANSWER);
        String replyIrbUrl = baseUrl + "/irb/irb_detail.html?id=" + saved.getId();
        String replyEmailHtml = emailTemplateService.buildIrbReplyEmail(saved.getTitle(), replyIrbUrl);
        emailService.sendEmails(reply.getEmails(), "[휴먼피부임상시험센터] IRB 재심사참여 요청 - " + saved.getTitle(), replyEmailHtml);

        List<FileResponseDTO> fileResponses = null;

        if (reply.getFiles() != null && !reply.getFiles().isEmpty()) {
            fileResponses = fileService.getMultipleUploadLinks(
                    saved.getId(),
                    EntityType.IRB,
                    EntityCategory.ATTACHMENT,
                    reply.getUserId(),
                    reply.getFiles());
        }

        return irbMapper.toDto(saved,fileResponses);
    }
    
    //irb 수정
    @Transactional
    public IrbTestResponseDTO update(Long id, IrbTestRequestDTO irbRequestDTO) {
        IrbTestEntity find = irbTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 irb를 찾을 수 없습니다. ID: " + id));

        // 1. 내용 변경 여부 체크
        boolean contentChanged = !Objects.equals(find.getTitle(), irbRequestDTO.getTitle())
                || !Objects.equals(find.getStartDate(), irbRequestDTO.getStartDate())
                || !Objects.equals(find.getEndDate(), irbRequestDTO.getEndDate())
                || !Objects.equals(find.getCategoryId(), irbRequestDTO.getCategoryId());

        // 2. 엔티티 수정
        find.setTitle(irbRequestDTO.getTitle());
        find.setCategoryId(irbRequestDTO.getCategoryId());
        find.setStartDate(irbRequestDTO.getStartDate());
        find.setEndDate(irbRequestDTO.getEndDate());
        find.setIsTemp(irbRequestDTO.getIsTemp());
        if (irbRequestDTO.getStatus() != null) {
            find.setStatus(irbRequestDTO.getStatus().toString());
        }
        IrbTestEntity saved = irbTestRepository.save(find);

        // 3. 이메일 처리
        List<UserEmailDTO> newEmails = irbRequestDTO.getEmails() != null
                ? irbRequestDTO.getEmails() : new ArrayList<>();

        List<IrbEmailListEntity> existingLogs = irbEmailListRepository.findAllByIrbTestId(find.getId());

        // 기존 수신자 이메일 목록 추출 (이제 UserEmailDTO로 정상 역직렬화됨)
        List<String> existingEmails = existingLogs.stream()
                .flatMap(log -> log.getUserList() != null ? log.getUserList().stream() : java.util.stream.Stream.empty())
                .map(UserEmailDTO::getEmail)
                .distinct()
                .collect(Collectors.toList());

        // 추가된 수신자만 추출
        List<UserEmailDTO> addedRecipients = newEmails.stream()
                .filter(user -> !existingEmails.contains(user.getEmail()))
                .collect(Collectors.toList());

        // 이메일 로그 업데이트
        EmailType type = existingLogs.isEmpty() ? EmailType.NEW_POST
                : EmailType.fromString(existingLogs.get(0).getEmailType());
        emailListService.updateEmailLog(find.getIrbTestId(), newEmails, type);

        String irbUrl = baseUrl + "/irb/irb_detail.html?id=" + find.getId();
        String emailHtml = emailTemplateService.buildIrbNotificationEmail(find.getTitle(), irbUrl);

        if (contentChanged && !addedRecipients.isEmpty()) {
            // 1. 내용 + 수신자 변경 → 전체 수신자에게 발송
            emailService.sendEmails(newEmails,
                    "[휴먼피부임상시험센터] IRB 심사참여 요청 (수정) - " + find.getTitle(), emailHtml);
        } else if (!addedRecipients.isEmpty()) {
            // 2. 수신자만 추가 → 추가된 수신자에게만 발송
            emailService.sendEmails(addedRecipients,
                    "[휴먼피부임상시험센터] IRB 심사참여 요청 - " + find.getTitle(), emailHtml);
        } else if (contentChanged) {
            // 3. 내용만 변경 → 기존 전체 수신자에게 발송
            emailService.sendEmails(newEmails,
                    "[휴먼피부임상시험센터] IRB 심사참여 요청 (수정) - " + find.getTitle(), emailHtml);
        }

        // 4. 파일 삭제 처리
        if (irbRequestDTO.getRemoveFiles() != null && !irbRequestDTO.getRemoveFiles().isEmpty()) {
            irbRequestDTO.getRemoveFiles().forEach(removeFile -> {
                if (removeFile.getId() != null) {
                    fileService.delete(removeFile.getId());
                }
            });
        }

        // 5. 파일 추가 처리
        List<FileResponseDTO> newUploadLinks = new ArrayList<>();
        if (irbRequestDTO.getFiles() != null && !irbRequestDTO.getFiles().isEmpty()) {
            newUploadLinks = fileService.getMultipleUploadLinks(
                    id, EntityType.IRB, EntityCategory.ATTACHMENT,
                    irbRequestDTO.getUserId(), irbRequestDTO.getFiles());
        }

        // 6. 최종 파일 목록 조회
        List<FileResponseDTO> currentFiles = fileService.getFilesByEntity(EntityType.IRB, id);
        return irbMapper.toDto(saved, currentFiles);
    }
    
    //irb 삭제
    public void delete(Long id){
        fileService.deleteAllByEntity(EntityType.IRB,id);
        emailListService.delete(id);
        irbTestRepository.deleteById(id);
    }
}