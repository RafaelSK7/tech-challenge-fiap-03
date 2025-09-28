package fiap.tech.challenge.hospital_manager.repository.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
