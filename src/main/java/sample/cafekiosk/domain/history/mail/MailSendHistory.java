package sample.cafekiosk.domain.history.mail;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.domain.product.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MailSendHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIL_SEND_HISTORY_SQ")
    @SequenceGenerator(sequenceName = "MAIL_SEND_HISTORY_SQ", name = "MAIL_SEND_HISTORY_SQ", allocationSize = 1)
    private Long id;

    private String fromEmail;
    private String toEmail;
    private String subject;
    private String content;


    @Builder
    public MailSendHistory(String fromEmail, String toEmail, String subject, String content) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }
}
