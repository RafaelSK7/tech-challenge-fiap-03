package fiap.tech.challenge.hospital_manager.repository.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}
