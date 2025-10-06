package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreatePacienteUseCase {

    private PacienteRepository pacienteRepository;

    private PasswordEncoder encoder;

    public CreatePacienteUseCase (PacienteRepository pacienteRepository, PasswordEncoder encoder){
        this.pacienteRepository = pacienteRepository;
        this.encoder = encoder;
    }

    public Paciente createPaciente(PacienteIn pacienteRequest) {
        Paciente paciente = new Paciente(pacienteRequest, encoder);
        return pacienteRepository.save(paciente);
    }
}
