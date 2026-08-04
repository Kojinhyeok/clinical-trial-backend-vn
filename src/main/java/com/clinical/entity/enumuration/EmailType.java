package com.clinical.entity.enumuration;

public enum EmailType {
    NEW_ANSWER, NEW_POST, ANSWER_COMPLETE,
    INVITE, RESEND_INVITE, PASSWORD_RESET;

    // 문자열을 안전하게 Enum으로 변환하는 정적 메서드
    public static EmailType fromString(String text) {
        if (text == null) return null;

        try {
            // 대소문자 구분을 없애고 싶다면 .toUpperCase() 추가
            return EmailType.valueOf(text.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // 일치하는 항목이 없을 때 null을 리턴하거나 기본값(default)을 리턴
            return null;
        }
    }
}
