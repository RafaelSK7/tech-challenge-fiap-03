package fiap.tech.challenge.hospital_manager.exception.custom;

public class ProfissionalNotFoundException extends RuntimeException {

    public ProfissionalNotFoundException(Long idProfissional) {
        super(String.format("Profissional com id: %s nao encontrado", idProfissional));
    }
}
