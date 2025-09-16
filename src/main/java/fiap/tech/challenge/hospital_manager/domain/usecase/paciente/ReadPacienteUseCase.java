package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.exception.custom.PacienteNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadPacienteUseCase {

    private PacienteRepository pacienteRepository;

    public ReadPacienteUseCase(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Page<Paciente> findAll(Pageable page) {
        return pacienteRepository.findAll(page);
    }

    public Paciente findById(Long idPaciente) {
        return pacienteRepository.findById(idPaciente).orElseThrow(() -> new PacienteNotFoundException(idPaciente));
    }
}
