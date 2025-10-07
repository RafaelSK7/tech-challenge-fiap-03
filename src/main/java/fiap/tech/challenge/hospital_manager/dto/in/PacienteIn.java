package fiap.tech.challenge.hospital_manager.dto.in;

public record PacienteIn(
        String nomePaciente
        , String nomeConvenio
        , String carteirinhaConvenio
        , String username
        , String password
) {
}
