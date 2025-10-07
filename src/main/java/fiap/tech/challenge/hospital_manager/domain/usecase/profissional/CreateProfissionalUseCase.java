package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateProfissionalUseCase {

    private ProfissionalRepository profissionalRepository;

    private PasswordEncoder encoder;

    public CreateProfissionalUseCase(ProfissionalRepository profissionalRepository, PasswordEncoder encoder) {
        this.profissionalRepository = profissionalRepository;
        this.encoder = encoder;
    }

    public Profissional createProfissional(ProfissionalIn profissionalIn) {
        Profissional profissional = new Profissional(profissionalIn, encoder);
        return profissionalRepository.save(profissional);
    }
}
