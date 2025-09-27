package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateProfissionalUseCase {

    private ProfissionalRepository profissionalRepository;

    private ReadProfissionalUseCase readProfissionalUseCase;

    public UpdateProfissionalUseCase(ProfissionalRepository profissionalRepository, ReadProfissionalUseCase readProfissionalUseCase) {
        this.profissionalRepository = profissionalRepository;
        this.readProfissionalUseCase = readProfissionalUseCase;
    }

    public Profissional updateProfissional(ProfissionalIn profissionalIn, Long id) {
        Profissional updated = readProfissionalUseCase.findById(id);
        updated.setNomeProfissional(profissionalIn.nomeProfissional());
        updated.setEspecialidades(profissionalIn.especialidades());
        updated.setConselhoRegional(profissionalIn.conselhoRegional());
        return profissionalRepository.save(updated);
    }
}
