package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DesmarcarConsultaUseCase {

    private ConsultaRepository consultaRepository;

    private ReadConsultaUseCase readConsultaUseCase;

    public DesmarcarConsultaUseCase(ConsultaRepository consultaRepository, ReadConsultaUseCase readConsultaUseCase) {
        this.consultaRepository = consultaRepository;
        this.readConsultaUseCase = readConsultaUseCase;
    }

    public Boolean deleteConsulta(Long idConsulta) {
        try {
            Consulta deletedConsulta = readConsultaUseCase.findById(idConsulta);
            consultaRepository.delete(deletedConsulta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
