package fiap.tech.challenge.hospital_manager.dto.out;

public record PacienteOut(
        Long pacienteId,
        String nomePaciente,
        String nomeConvenio
) {
}
