package fiap.tech.challenge.hospital_manager.producer;

import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificationPublisherService notificationPublisherService;

    private String mensagem;

    @BeforeEach
    void setUp() {
        mensagem = "Consulta marcada com sucesso";
    }

    @Test
    void deveEnviarMensagemParaExchangeCorreta() {
        // Act
        notificationPublisherService.sendNewNotification(mensagem);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitConfig.EXCHANGE_NOTIFICATION_NAME,
                RabbitConfig.ROUTING_KEY_NOTIFICATION,
                mensagem
        );
        verifyNoMoreInteractions(rabbitTemplate);
    }
}
