package fiap.tech.challenge.hospital_manager.service.profissional;

import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.CreateProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.DeleteProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.ReadProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.UpdateProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.dto.out.ProfissionalOut;
import fiap.tech.challenge.hospital_manager.presenter.profissional.ProfissionalPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfissionalService {

    private CreateProfissionalUseCase createProfissionalUseCase;
    private ReadProfissionalUseCase readProfissionalUseCase;
    private UpdateProfissionalUseCase updateProfissionalUseCase;
    private DeleteProfissionalUseCase deleteProfissionalUseCase;

    public ProfissionalService(CreateProfissionalUseCase createProfissionalUseCase, ReadProfissionalUseCase readProfissionalUseCase, UpdateProfissionalUseCase updateProfissionalUseCase, DeleteProfissionalUseCase deleteProfissionalUseCase) {
        this.createProfissionalUseCase = createProfissionalUseCase;
        this.readProfissionalUseCase = readProfissionalUseCase;
        this.updateProfissionalUseCase = updateProfissionalUseCase;
        this.deleteProfissionalUseCase = deleteProfissionalUseCase;
    }


    public Page<ProfissionalOut> findAll(Pageable page) {
        return readProfissionalUseCase.findAll(page).map(ProfissionalPresenter::toResponse);
    }

    public ProfissionalOut findById(Long id) {
        return ProfissionalPresenter.toResponse(readProfissionalUseCase.findById(id));
    }

    public ProfissionalOut createProfissional(ProfissionalIn profissionalIn) {
        return ProfissionalPresenter.toResponse(createProfissionalUseCase.createProfissional(profissionalIn));
    }

    public ProfissionalOut updateProfissional(ProfissionalIn profissionalIn, Long id) {
        return ProfissionalPresenter.toResponse(updateProfissionalUseCase.updateProfissional(profissionalIn, id));
    }

    public void deleteProfissional(Long id) {
        deleteProfissionalUseCase.deleteProfissional(id);
    }
}
