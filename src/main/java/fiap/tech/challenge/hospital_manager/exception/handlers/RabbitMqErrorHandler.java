package fiap.tech.challenge.hospital_manager.exception.handlers;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@Slf4j
public class RabbitMqErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("Erro ao processar mensagem RabbitMQ", t);
    }

    public void handleInvalidMessage(Message message, Channel channel, Exception e) {
        try {
            log.warn("Mensagem inválida descartada: {} - Motivo: {}", new String(message.getBody()), e.getMessage());
            // Rejeita a mensagem sem reencaminhar (evita loop infinito)
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            log.error("Erro ao descartar mensagem do RabbitMQ", ex);
        }
    }
}
