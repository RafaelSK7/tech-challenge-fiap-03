package fiap.tech.challenge.hospital_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsulta;

    private LocalDateTime dataHoraConsulta;

    @ManyToOne()
    @JoinColumn(name = "paciente_id", referencedColumnName = "idPaciente")
    @JsonBackReference
    private Paciente paciente;

    @ManyToOne()
    @JoinColumn(name = "profissional_id", referencedColumnName = "idProfissional")
    @JsonBackReference
    private Profissional profissional;

    private Especialidade especialidade;

    public Consulta(LocalDateTime dataHoraConsulta, Paciente paciente, Profissional profissional, Especialidade especialidade) {
        this.dataHoraConsulta = dataHoraConsulta;
        this.paciente = paciente;
        this.profissional = profissional;
        this.especialidade = especialidade;
    }
}
