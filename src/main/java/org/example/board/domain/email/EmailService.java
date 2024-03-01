package org.example.board.domain.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.board.domain.user.VerificationService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String SENDER_EMAIL = "moz1mozi@example.com";
    public static int number;
    private final JavaMailSender javaMailSender;
    private final VerificationRepository verificationRepository;
    private final VerificationService verificationService;

    //인증 번호 생성
    @Transactional
    public void sendVerificationEmail(String to, String verificationCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(SENDER_EMAIL);
            helper.setTo(to);
            helper.setSubject("회원가입 인증 코드");
            String content = "<h3> 요청하신 인증 번호입니다. </h3>" +
                             "<h1>" + verificationCode + "</h1>" +
                             "<h3> 감사합니다. </h3>";
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("이메일 발송 실패 = {}", ex.getMessage());
        }
    }

}
