package fiap.tech.challenge.hospital_manager.service.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.DesmarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.ReadConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.UpdateConsultaUseCase;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ConsultaService {

    private ReadConsultaUseCase readConsultaUseCase;

    private UpdateConsultaUseCase updateConsultaUseCase;

    private DesmarcarConsultaUseCase desmarcarConsultaUseCase;

    public ConsultaService(ReadConsultaUseCase readConsultaUseCase, UpdateConsultaUseCase updateConsultaUseCase, DesmarcarConsultaUseCase desmarcarConsultaUseCase) {
        this.readConsultaUseCase = readConsultaUseCase;
        this.updateConsultaUseCase = updateConsultaUseCase;
        this.desmarcarConsultaUseCase = desmarcarConsultaUseCase;
    }

    public List<Consulta> findAll() {
        return readConsultaUseCase.findAll();
    }

    public Consulta findById(Long id) {
        return readConsultaUseCase.findById(id);
    }

    public Consulta updateConsulta(ConsultaIn consultaIn, Long id) {
        return updateConsultaUseCase.updateConsulta(consultaIn, id);
    }

    public Boolean desmarcarConsulta(Long id) {
        return desmarcarConsultaUseCase.deleteConsulta(id);
    }
}
