package fiap.tech.challenge.hospital_manager.dto.out;

import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;

import java.util.List;

public record ProfissionalOut(

        Long idProfissional,
        String nomeProfissional,
        List<Especialidade> especialidades,
        Integer conselhoRegional
) {
}
