package com.clinical.service.email;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    private static final String BRAND_COLOR = "#3b7ddd";

    public String buildInviteEmail(String name, String signupUrl) {
        String body = "<h2 style='color:#333;margin:0 0 10px;'>회원가입 초대</h2>"
                + "<p style='color:#555;font-size:15px;line-height:1.6;'>"
                + "<strong>" + escapeHtml(name) + "</strong>님, 안녕하세요.<br>"
                + "휴먼피부임상시험센터 시스템에 초대되었습니다.<br>"
                + "아래 버튼을 클릭하여 회원가입을 완료해 주세요.</p>"
                + buildButton("회원가입 하기", signupUrl)
                + "<p style='color:#999;font-size:13px;margin-top:20px;'>"
                + "본 링크는 7일간 유효합니다.<br>"
                + "링크가 만료된 경우 관리자에게 재발송을 요청해 주세요.</p>";
        return wrapLayout(body);
    }

    public String buildPasswordResetEmail(String name, String resetUrl) {
        String body = "<h2 style='color:#333;margin:0 0 10px;'>비밀번호 재설정</h2>"
                + "<p style='color:#555;font-size:15px;line-height:1.6;'>"
                + "<strong>" + escapeHtml(name) + "</strong>님, 안녕하세요.<br>"
                + "비밀번호 재설정 요청이 접수되었습니다.<br>"
                + "아래 버튼을 클릭하여 새 비밀번호를 설정해 주세요.</p>"
                + buildButton("비밀번호 재설정", resetUrl)
                + "<p style='color:#999;font-size:13px;margin-top:20px;'>"
                + "본 링크는 30분간 유효합니다.<br>"
                + "본인이 요청하지 않은 경우 이 메일을 무시하셔도 됩니다.</p>";
        return wrapLayout(body);
    }

    public String buildIrbNotificationEmail(String title, String irbUrl) {
        String body = "<h2 style='color:#333;margin:0 0 10px;'>IRB 심사참여 요청</h2>"
                + "<p style='color:#555;font-size:15px;line-height:1.6;'>"
                + "새로운 IRB 심사가 등록되었습니다.<br>"
                + "아래 내용을 확인하시고 심사에 참여해 주세요.</p>"
                + "<div style='background:#f8f9fa;border:1px solid #e9ecef;border-radius:6px;padding:16px;margin:16px 0;'>"
                + "<strong style='color:#333;'>제목:</strong> " + escapeHtml(title)
                + "</div>"
                + buildButton("심사 참여하기", irbUrl);
        return wrapLayout(body);
    }

    public String buildIrbReplyEmail(String title, String irbUrl) {
        String body = "<h2 style='color:#333;margin:0 0 10px;'>IRB 재심사참여 요청</h2>"
                + "<p style='color:#555;font-size:15px;line-height:1.6;'>"
                + "IRB 심사에 새로운 답변이 등록되었습니다.<br>"
                + "아래 내용을 확인하시고 재심사에 참여해 주세요.</p>"
                + "<div style='background:#f8f9fa;border:1px solid #e9ecef;border-radius:6px;padding:16px;margin:16px 0;'>"
                + "<strong style='color:#333;'>제목:</strong> " + escapeHtml(title)
                + "</div>"
                + buildButton("재심사 참여하기", irbUrl);
        return wrapLayout(body);
    }

    private String wrapLayout(String bodyContent) {
        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='margin:0;padding:0;background-color:#f5f7fb;font-family:-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,\"Helvetica Neue\",Arial,sans-serif;'>"
                + "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f5f7fb;padding:40px 0;'>"
                + "<tr><td align='center'>"
                + "<table width='600' cellpadding='0' cellspacing='0' style='background:#fff;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.08);'>"
                + "<tr><td style='background:" + BRAND_COLOR + ";padding:24px 32px;border-radius:8px 8px 0 0;'>"
                + "<h1 style='margin:0;color:#fff;font-size:20px;font-weight:600;'>Human Clinical</h1>"
                + "<p style='margin:4px 0 0;color:rgba(255,255,255,0.8);font-size:13px;'>휴먼피부임상시험센터 베트남</p>"
                + "</td></tr>"
                + "<tr><td style='padding:32px;'>"
                + bodyContent
                + "</td></tr>"
                + "<tr><td style='padding:20px 32px;border-top:1px solid #e9ecef;'>"
                + "<p style='margin:0;color:#999;font-size:12px;text-align:center;'>"
                + "본 메일은 발신 전용입니다. 문의사항이 있으시면 관리자에게 연락해 주세요."
                + "</p>"
                + "</td></tr>"
                + "</table>"
                + "</td></tr></table>"
                + "</body></html>";
    }

    private String buildButton(String text, String url) {
        return "<div style='text-align:center;margin:24px 0;'>"
                + "<a href='" + url + "' style='"
                + "display:inline-block;"
                + "padding:12px 32px;"
                + "background-color:" + BRAND_COLOR + ";"
                + "color:#fff;"
                + "text-decoration:none;"
                + "border-radius:6px;"
                + "font-size:15px;"
                + "font-weight:600;"
                + "'>" + escapeHtml(text) + "</a>"
                + "</div>";
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}