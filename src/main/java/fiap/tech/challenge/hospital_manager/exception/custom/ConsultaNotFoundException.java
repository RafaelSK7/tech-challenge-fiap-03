package fiap.tech.challenge.hospital_manager.exception.custom;

public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(Long id) {
        super(String.format("Consulta com id: %s nao encontrada", id));
    }
}
