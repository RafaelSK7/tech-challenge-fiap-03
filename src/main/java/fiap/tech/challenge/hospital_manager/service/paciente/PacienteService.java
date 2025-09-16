package fiap.tech.challenge.hospital_manager.service.paciente;

import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.CreatePacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.DeletePacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.ReadPacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.UpdatePacienteUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;
import fiap.tech.challenge.hospital_manager.presenter.paciente.PacientePresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PacienteService {

    private ReadPacienteUseCase readPacienteUseCase;

    private CreatePacienteUseCase createPacienteUseCase;

    private UpdatePacienteUseCase updatePacienteUseCase;

    private DeletePacienteUseCase deletePacienteUseCase;

    public PacienteService(ReadPacienteUseCase readPacienteUseCase, CreatePacienteUseCase createPacienteUseCase, UpdatePacienteUseCase updatePacienteUseCase, DeletePacienteUseCase deletePacienteUseCase){
        this.readPacienteUseCase = readPacienteUseCase;
        this.createPacienteUseCase = createPacienteUseCase;
        this.updatePacienteUseCase = updatePacienteUseCase;
        this.deletePacienteUseCase = deletePacienteUseCase;
    }

    public Page<PacienteOut> findAll(Pageable page) {
        return readPacienteUseCase.findAll(page).map(PacientePresenter::toResponse);
    }

    public PacienteOut findById(Long id) {
        return PacientePresenter.toResponse(readPacienteUseCase.findById(id));
    }

    public PacienteOut createPaciente(PacienteIn pacienteRequest) {
        return PacientePresenter.toResponse(createPacienteUseCase.createPaciente(pacienteRequest));
    }

    public PacienteOut updatePaciente(PacienteIn pacienteRequest, Long id) {
        return PacientePresenter.toResponse(updatePacienteUseCase.updatePaciente(pacienteRequest, id));
    }

    public void deletePaciente(Long id) {
        deletePacienteUseCase.deletePaciente(id);
    }
}