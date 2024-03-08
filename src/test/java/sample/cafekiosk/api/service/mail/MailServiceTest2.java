package sample.cafekiosk.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.client.mail.MailSendClient;
import sample.cafekiosk.domain.history.mail.MailSendHistory;
import sample.cafekiosk.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

//@SpringBootTest
//@Transactional
//@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class MailServiceTest2 {
    @Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    // Mock 객체를 inject 해준다
    @InjectMocks
    private MailService mailService;


    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
//        Mockito.when(mailSendClient.sendEmail(anyString(), anyString(),anyString(),anyString()))
//                .thenReturn(true);
        // @Spy를 이용해서 실제 빈을 주입하고 아래 doReturn을 통해 해당 메소드는 무조건 true를 반환하게 한다는 조건으로 테스트
        doReturn(true)
                .when(mailSendClient)
                .sendEmail(anyString(), anyString(), anyString(), anyString());


        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        // save의 행위가 한번 호출되었는지 확인
        assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));


    }
}