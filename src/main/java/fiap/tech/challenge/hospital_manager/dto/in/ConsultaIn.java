package fiap.tech.challenge.hospital_manager.dto.in;

import java.time.LocalDateTime;

public record ConsultaIn(
        LocalDateTime dataHoraConsulta,
        Long idPaciente,
        Long idProfissional,
        String especialidade
) {
}
