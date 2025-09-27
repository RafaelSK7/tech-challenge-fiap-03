package fiap.tech.challenge.hospital_manager.dto.in;

import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;

import java.util.List;

public record ProfissionalIn(

        String nomeProfissional,
        List<Especialidade>especialidades,
        Integer conselhoRegional
) {
}
