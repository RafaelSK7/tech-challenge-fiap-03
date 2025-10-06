package fiap.tech.challenge.hospital_manager.consumer.cosulta;

import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.MarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.ConsultaIn;
import fiap.tech.challenge.hospital_manager.exception.handlers.RabbitMqErrorHandler;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
@Slf4j
public class ConsultaConsumer {

    private final MarcarConsultaUseCase marcarConsultaUseCase;
    private final RabbitMqErrorHandler errorHandler;

    public ConsultaConsumer(MarcarConsultaUseCase marcarConsultaUseCase, RabbitMqErrorHandler errorHandler) {
        this.marcarConsultaUseCase = marcarConsultaUseCase;
        this.errorHandler = errorHandler;
    }

    @RabbitListener(queues = RabbitConfig.CONSULTA_QUEUE)
    public void marcarConsulta(ConsultaIn consultaIn, Message message, Channel channel) {
        try {
            marcarConsultaUseCase.marcarConsulta(consultaIn);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            errorHandler.handleInvalidMessage(consultaIn, message, channel, e);
        }
    }

}
