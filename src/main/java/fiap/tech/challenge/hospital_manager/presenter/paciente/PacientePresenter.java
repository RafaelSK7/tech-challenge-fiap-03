package fiap.tech.challenge.hospital_manager.presenter.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;

public class PacientePresenter {

    public static PacienteOut toResponse(Paciente paciente) {
        return new PacienteOut(paciente.getIdPaciente(), paciente.getNomePaciente(), paciente.getNomeConvenio());
    }
}

