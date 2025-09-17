package fiap.tech.challenge.hospital_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @NotBlank
    private String nomePaciente;

    @NotBlank
    private String nomeConvenio;
    
    @NotBlank
    private String carteirinhaConvenio;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Consulta> consultas;

    public Paciente (PacienteIn pacienteRequest){
        this.setNomePaciente(pacienteRequest.nomePaciente());
        this.setNomeConvenio(pacienteRequest.nomeConvenio());
        this.setCarteirinhaConvenio(pacienteRequest.carteirinhaConvenio());
    }
}
