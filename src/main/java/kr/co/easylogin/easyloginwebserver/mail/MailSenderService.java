package kr.co.easylogin.easyloginwebserver.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.easylogin.easyloginwebserver.common.dto.value.ResponseCode;
import kr.co.easylogin.easyloginwebserver.common.error.BusinessException;
import kr.co.easylogin.easyloginwebserver.member.dto.request.EmailVerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMailVerificationCode(EmailVerificationRequest request, String code) throws MessagingException {
        MimeMessage mimeMessage = createMimeMessage(request, code);
        try {
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("메일 발송중 오류 발생 : {}", e.getMessage());
            throw new BusinessException(ResponseCode.MAIL_CODE_CREATE_ERROR);
        }
    }

    private MimeMessage createMimeMessage(EmailVerificationRequest request, String code) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            helper.setTo(request.getEmail());
            helper.setSubject("[이지로그인] 이메일 인증을 진행해주세요");
            helper.setText(setContext(code), true);
        } catch (Exception e) {
            log.error("이메일 MimeMessage 생성중 오류 발생 : {}", e.getMessage());
            throw new BusinessException(ResponseCode.MAIL_CODE_CREATE_ERROR);
        }

        return mimeMessage;
    }

    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("emailVerification", context);
    }
}
