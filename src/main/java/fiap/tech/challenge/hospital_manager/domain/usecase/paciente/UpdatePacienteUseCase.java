package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.entity.Usuario;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdatePacienteUseCase {

    private ReadPacienteUseCase readPacienteUseCase;

    private PacienteRepository pacienteRepository;

    public UpdatePacienteUseCase(ReadPacienteUseCase readPacienteUseCase, PacienteRepository pacienteRepository) {
        this.readPacienteUseCase = readPacienteUseCase;
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente updatePaciente(PacienteIn pacienteRequest, Long id) {

        Paciente pacienteToUpdate = readPacienteUseCase.findById(id);
        pacienteToUpdate.setNomePaciente(pacienteRequest.nomePaciente());
        pacienteToUpdate.setNomeConvenio(pacienteRequest.nomeConvenio());
        pacienteToUpdate.setCarteirinhaConvenio(pacienteRequest.carteirinhaConvenio());

        Usuario usuario = pacienteToUpdate.getUsuario();
        usuario.setUsername(pacienteRequest.username());
        usuario.setPassword(pacienteRequest.password());

        pacienteToUpdate.setUsuario(usuario);

        return pacienteRepository.save(pacienteToUpdate);
    }
}
