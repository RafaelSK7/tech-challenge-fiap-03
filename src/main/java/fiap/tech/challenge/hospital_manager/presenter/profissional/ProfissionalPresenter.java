package fiap.tech.challenge.hospital_manager.presenter.profissional;


import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.dto.out.ProfissionalOut;

public class ProfissionalPresenter {

    public static ProfissionalOut toResponse(Profissional profissional) {
        return new ProfissionalOut(profissional.getIdProfissional(), profissional.getNomeProfissional(), profissional.getEspecialidades(), profissional.getConselhoRegional());
    }
}
