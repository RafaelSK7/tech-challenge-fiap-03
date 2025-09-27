package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;


import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteProfissionalUseCase {

    private ProfissionalRepository profissionalRepository;

    private ReadProfissionalUseCase readProfissionalUseCase;

    public DeleteProfissionalUseCase(ProfissionalRepository profissionalRepository, ReadProfissionalUseCase readProfissionalUseCase) {
        this.profissionalRepository = profissionalRepository;
        this.readProfissionalUseCase = readProfissionalUseCase;
    }

    public void deleteProfissional(Long id) {
        Profissional deleted = readProfissionalUseCase.findById(id);
        profissionalRepository.delete(deleted);
    }
}
