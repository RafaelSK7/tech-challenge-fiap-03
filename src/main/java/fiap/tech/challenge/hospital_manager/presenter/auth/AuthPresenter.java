package fiap.tech.challenge.hospital_manager.presenter.auth;

import fiap.tech.challenge.hospital_manager.dto.out.AuthOut;

public class AuthPresenter {

    public static AuthOut toResponse(String token) {
        return new AuthOut(token);
    }
}
