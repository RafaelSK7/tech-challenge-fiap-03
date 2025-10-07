package fiap.tech.challenge.hospital_manager.exception.custom;

public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(){
        super("Token JWT ausente");
    }
}
