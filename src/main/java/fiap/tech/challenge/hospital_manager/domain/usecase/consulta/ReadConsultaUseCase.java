package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.exception.custom.ConsultaNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReadConsultaUseCase {

    private ConsultaRepository consultaRepository;

    public ReadConsultaUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }


    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Consulta findById(Long idConsulta) {
        return consultaRepository.findById(idConsulta).orElseThrow(() -> new ConsultaNotFoundException(idConsulta));
    }
}
