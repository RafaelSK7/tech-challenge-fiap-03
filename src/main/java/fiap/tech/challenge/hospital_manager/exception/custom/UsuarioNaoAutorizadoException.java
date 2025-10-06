package fiap.tech.challenge.hospital_manager.exception.custom;

import org.springframework.stereotype.Component;

@Component
public class UsuarioNaoAutorizadoException extends Throwable {

    public UsuarioNaoAutorizadoException() {
        super("Usuário não autorizado a utilizar este recurso.");
    }
}
