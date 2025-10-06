package fiap.tech.challenge.hospital_manager.producer;

import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisherService {

    private RabbitTemplate rabbitTemplate;

    public NotificationPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNewNotification(String msg) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NOTIFICATION_NAME,
                RabbitConfig.ROUTING_KEY_NOTIFICATION,
                msg
        );
    }
}
