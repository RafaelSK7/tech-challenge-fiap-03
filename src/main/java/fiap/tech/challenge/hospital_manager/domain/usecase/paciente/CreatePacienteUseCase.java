package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreatePacienteUseCase {

    private PacienteRepository pacienteRepository;

    public CreatePacienteUseCase (PacienteRepository pacienteRepository){
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente createPaciente(PacienteIn pacienteRequest) {
        Paciente paciente = new Paciente(pacienteRequest);
        return pacienteRepository.save(paciente);
    }
}
