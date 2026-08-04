package com.clinical.service.email;

public interface EmailSender {
    void send(String to, String subject, String htmlBody);
}