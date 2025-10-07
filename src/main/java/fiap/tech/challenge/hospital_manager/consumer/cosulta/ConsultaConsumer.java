package fiap.tech.challenge.hospital_manager.consumer.cosulta;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.MarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.producer.NotificationPublisherService;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import fiap.tech.challenge.hospital_manager.exception.custom.TokenNotFoundException;
import fiap.tech.challenge.hospital_manager.exception.custom.UsuarioNaoAutorizadoException;
import fiap.tech.challenge.hospital_manager.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import fiap.tech.challenge.hospital_manager.exception.handlers.RabbitMqErrorHandler;

@Component
@Slf4j
public class ConsultaConsumer {

    private MarcarConsultaUseCase marcarConsultaUseCase;
    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;
    private NotificationPublisherService notificationPublisherService;
    private final RabbitMqErrorHandler errorHandler;

    public ConsultaConsumer(MarcarConsultaUseCase marcarConsultaUseCase, JwtUtil jwtUtil, ObjectMapper objectMapper, NotificationPublisherService notificationPublisherService, RabbitMqErrorHandler errorHandler) {
        this.marcarConsultaUseCase = marcarConsultaUseCase;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.notificationPublisherService = notificationPublisherService;
        this.errorHandler = errorHandler;
    }

    @RabbitListener(queues = RabbitConfig.CONSULTA_QUEUE)
    public void marcarConsulta(Message message, Channel channel) throws UsuarioNaoAutorizadoException {
        try {
            String authorizationHeader = message.getMessageProperties().getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
                throw new TokenNotFoundException();
            }

            String token = authorizationHeader.substring(7);

            // 2️⃣ Extrai role do token
            String role = jwtUtil.getRoleFromToken(token);

            if (!"ROLE_ENFERMEIRO".equals(role)) {
                throw new UsuarioNaoAutorizadoException();
            }

            String body = new String(message.getBody());

            ConsultaIn consultaIn = objectMapper.readValue(body, ConsultaIn.class);

            marcarConsultaUseCase.marcarConsulta(consultaIn);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            notificationPublisherService.sendNewNotification("Sua consulta foi agendada com sucesso.");
        } catch (Exception e) {
            errorHandler.handleInvalidMessage(message, channel, e);
        }
    }
}