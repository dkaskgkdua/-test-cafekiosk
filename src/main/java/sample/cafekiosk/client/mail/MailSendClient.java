package sample.cafekiosk.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
        // 메일 전송
        log.info("메일 전송");
        throw new IllegalArgumentException("메일 전송");

//        return true;
    }

    public void a() {
        System.out.println("a");
    }
    public void b() {
        System.out.println("b");
    }
    public void c() {
        System.out.println("c");
    }
}
