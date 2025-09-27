package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.exception.custom.ProfissionalNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadProfissionalUseCase {

    private ProfissionalRepository profissionalRepository;

    public ReadProfissionalUseCase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Page<Profissional> findAll(Pageable page) {
        return profissionalRepository.findAll(page);
    }

    public Profissional findById(Long idProfissional) {
        return profissionalRepository.findById(idProfissional).orElseThrow(() -> new ProfissionalNotFoundException(idProfissional));
    }
}
