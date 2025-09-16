package fiap.tech.challenge.hospital_manager.repository.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
