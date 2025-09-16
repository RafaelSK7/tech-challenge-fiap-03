package fiap.tech.challenge.hospital_manager.exception.custom;

public class PacienteNotFoundException extends RuntimeException {

    public PacienteNotFoundException(Long idPaciente) {
        super(String.format("Paciente com id: %s nao encontrado", idPaciente));
    }
}
