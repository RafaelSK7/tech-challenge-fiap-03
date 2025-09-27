package fiap.tech.challenge.hospital_manager.exception.custom;

public class EspecialidadeNotFoundException extends RuntimeException {

    public EspecialidadeNotFoundException(){
        super("Especialidade nao atendida pelo profissional");
    }
}
