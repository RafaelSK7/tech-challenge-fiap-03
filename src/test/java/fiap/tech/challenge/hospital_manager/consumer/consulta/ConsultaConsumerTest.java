package fiap.tech.challenge.hospital_manager.consumer.consulta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.MarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.exception.custom.TokenNotFoundException;
import fiap.tech.challenge.hospital_manager.exception.custom.UsuarioNaoAutorizadoException;
import fiap.tech.challenge.hospital_manager.exception.handlers.RabbitMqErrorHandler;
import fiap.tech.challenge.hospital_manager.producer.NotificationPublisherService;
import fiap.tech.challenge.hospital_manager.security.JwtUtil;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaConsumerTest {

    @Mock
    private MarcarConsultaUseCase marcarConsultaUseCase;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private NotificationPublisherService notificationPublisherService;

    @Mock
    private RabbitMqErrorHandler errorHandler;

    @Mock
    private Channel channel;

    @InjectMocks
    private ConsultaConsumer consultaConsumer;

    private MessageProperties messageProperties;
    private ConsultaIn consultaIn;

    @BeforeEach
    void setup() {
        messageProperties = new MessageProperties();
        consultaIn = new ConsultaIn(LocalDateTime.now(),1L, 2L, "CLINICA_GERAL");
    }

    @Test
    void deveMarcarConsultaComSucesso() throws Exception {
        // Arrange
        messageProperties.setHeader("Authorization", "Bearer token123");
        Message message = new Message("{}".getBytes(), messageProperties);

        when(jwtUtil.getRoleFromToken("token123")).thenReturn("ROLE_ENFERMEIRO");
        when(objectMapper.readValue(anyString(), eq(ConsultaIn.class))).thenReturn(consultaIn);

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(marcarConsultaUseCase).marcarConsulta(consultaIn);
        verify(channel).basicAck(message.getMessageProperties().getDeliveryTag(), false);
        verify(notificationPublisherService).sendNewNotification("Sua consulta foi agendada com sucesso.");
        verify(errorHandler, never()).handleInvalidMessage(any(), any(), any());
    }

    @Test
    void deveLancarTokenNotFoundQuandoHeaderAusente() throws Exception {
        // Arrange
        Message message = new Message("{}".getBytes(), messageProperties);

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(errorHandler).handleInvalidMessage(eq(message), eq(channel), any(TokenNotFoundException.class));
        verifyNoInteractions(marcarConsultaUseCase);
    }

    @Test
    void deveLancarUsuarioNaoAutorizadoQuandoRoleInvalida() throws Exception {
        // Arrange
        messageProperties.setHeader("Authorization", "Bearer token123");
        Message message = new Message("{}".getBytes(), messageProperties);

        when(jwtUtil.getRoleFromToken("token123")).thenReturn("ROLE_MEDICO");

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(errorHandler).handleInvalidMessage(eq(message), eq(channel), any(UsuarioNaoAutorizadoException.class));
        verifyNoInteractions(marcarConsultaUseCase);
    }

    @Test
    void deveEncaminharErroParaHandlerQuandoFalhaNaDeserializacao() throws Exception {
        // Arrange
        messageProperties.setHeader("Authorization", "Bearer token123");
        Message message = new Message("{}".getBytes(), messageProperties);

        when(jwtUtil.getRoleFromToken("token123")).thenReturn("ROLE_ENFERMEIRO");
        when(objectMapper.readValue(anyString(), eq(ConsultaIn.class))).thenThrow(new RuntimeException("Erro ao ler JSON"));

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(errorHandler).handleInvalidMessage(eq(message), eq(channel), any(RuntimeException.class));
        verifyNoInteractions(marcarConsultaUseCase);
    }
}
