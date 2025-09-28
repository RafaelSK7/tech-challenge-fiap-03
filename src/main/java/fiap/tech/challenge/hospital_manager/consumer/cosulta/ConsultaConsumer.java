package fiap.tech.challenge.hospital_manager.consumer.cosulta;

import fiap.tech.challenge.hospital_manager.config.RabbitConfig;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.MarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.ConsultaIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsultaConsumer {

    private MarcarConsultaUseCase marcarConsultaUseCase;

    public ConsultaConsumer(MarcarConsultaUseCase marcarConsultaUseCase) {
        this.marcarConsultaUseCase = marcarConsultaUseCase;
    }

    @RabbitListener(queues = RabbitConfig.CONSULTA_QUEUE)
    public void marcarConsulta(ConsultaIn consultaIn) {
        marcarConsultaUseCase.marcarConsulta(consultaIn);
    }
}
