package fiap.tech.challenge.hospital_manager.consumer.consulta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import fiap.tech.challenge.hospital_manager.consumer.cosulta.ConsultaConsumer;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.MarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.exception.custom.TokenNotFoundException;
import fiap.tech.challenge.hospital_manager.exception.custom.UsuarioNaoAutorizadoException;
import fiap.tech.challenge.hospital_manager.exception.handlers.RabbitMqErrorHandler;
import fiap.tech.challenge.hospital_manager.producer.NotificationPublisherService;
import fiap.tech.challenge.hospital_manager.security.JwtUtil;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        consultaConsumer = new ConsultaConsumer(marcarConsultaUseCase, jwtUtil, objectMapper,
                notificationPublisherService, errorHandler);
    }

    @Test
    void deveMarcarConsulta_QuandoMensagemValidaRecebida() throws Exception, UsuarioNaoAutorizadoException {
        // Arrange
        MessageProperties props = new MessageProperties();
        props.setHeader("Authorization", "Bearer validtoken");
        Message message = new Message("{}".getBytes(), props);

        when(jwtUtil.getRoleFromToken("validtoken")).thenReturn("ROLE_ENFERMEIRO");
        when(objectMapper.readValue(anyString(), eq(ConsultaIn.class))).thenReturn(mock(ConsultaIn.class));

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(marcarConsultaUseCase).marcarConsulta(any(ConsultaIn.class));
        verify(channel).basicAck(anyLong(), eq(false));
        verify(notificationPublisherService).sendNewNotification(anyString());
        verify(errorHandler, never()).handleInvalidMessage(any(), any(), any());
    }

    @Test
    void deveChamarErrorHandler_QuandoTokenAusente() throws Exception, UsuarioNaoAutorizadoException {
        // Arrange
        MessageProperties props = new MessageProperties();
        Message message = new Message("{}".getBytes(), props);

        // Act
        consultaConsumer.marcarConsulta(message, channel);

        // Assert
        verify(errorHandler).handleInvalidMessage(eq(message), eq(channel), any(TokenNotFoundException.class));
        verify(marcarConsultaUseCase, never()).marcarConsulta(any());
    }

    @Test
    void deveChamarErrorHandler_QuandoUsuarioNaoAutorizado() {
        MessageProperties props = new MessageProperties();
        props.setHeader("Authorization", "Bearer invalidrole");
        Message message = new Message("{}".getBytes(), props);

        when(jwtUtil.getRoleFromToken("invalidrole")).thenReturn("ROLE_PACIENTE");

        assertThrows(UsuarioNaoAutorizadoException.class, () -> {
            consultaConsumer.marcarConsulta(message, channel);
        });

        verify(marcarConsultaUseCase, never()).marcarConsulta(any());
    }

    @Test
    void deveChamarErrorHandler_QuandoExceptionGenerica() throws Exception, UsuarioNaoAutorizadoException {

        MessageProperties props = new MessageProperties();
        props.setHeader("Authorization", "Bearer validtoken");
        Message message = new Message("{}".getBytes(), props);

        when(jwtUtil.getRoleFromToken("validtoken")).thenReturn("ROLE_ENFERMEIRO");
        when(objectMapper.readValue(anyString(), eq(ConsultaIn.class)))
                .thenThrow(new RuntimeException("Erro de parsing"));

        consultaConsumer.marcarConsulta(message, channel);

        verify(errorHandler).handleInvalidMessage(eq(message), eq(channel), any(RuntimeException.class));
        verify(marcarConsultaUseCase, never()).marcarConsulta(any());
    }
}