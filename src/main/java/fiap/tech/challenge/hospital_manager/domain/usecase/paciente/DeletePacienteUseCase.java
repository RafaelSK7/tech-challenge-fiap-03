package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeletePacienteUseCase {

    private ReadPacienteUseCase readPacienteUseCase;

    private PacienteRepository pacienteRepository;

    public DeletePacienteUseCase(ReadPacienteUseCase readPacienteUseCase, PacienteRepository pacienteRepository) {
        this.readPacienteUseCase = readPacienteUseCase;
        this.pacienteRepository = pacienteRepository;
    }

    public void deletePaciente(Long id) {
        Paciente pacienteToDelete = readPacienteUseCase.findById(id);
        pacienteRepository.delete(pacienteToDelete);
    }
}
