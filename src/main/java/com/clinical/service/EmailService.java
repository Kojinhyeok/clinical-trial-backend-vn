package com.clinical.service;

import com.clinical.dto.irb.UserEmailDTO;
import com.clinical.service.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSender emailSender;

    /**
     * 리스트에 담긴 모든 사용자에게 이메일 발송
     * @param userList 수신자 리스트 (UserEmailDTO: name, email 포함)
     * @param subject  메일 제목
     * @param content  메일 내용 (HTML 가능)
     */
    public void sendEmails(List<UserEmailDTO> userList, String subject, String content) {
        if (userList == null || userList.isEmpty()) {
            log.warn("수신자 리스트가 비어 있어 메일을 발송하지 않습니다.");
            return;
        }

        for (UserEmailDTO user : userList) {
            try {
                emailSender.send(user.getEmail(), subject, content);
            } catch (Exception e) {
                log.error("이메일 발송 실패 (계속 진행): {} - {}", user.getEmail(), e.getMessage());
            }
        }
    }

    /**
     * 단일 메일 발송
     */
    public void sendEmail(String toEmail, String subject, String content) {
        emailSender.send(toEmail, subject, content);
    }
}
