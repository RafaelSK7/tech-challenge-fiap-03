package fiap.tech.challenge.hospital_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario", nullable = false)
    private Usuario usuario;

    public Paciente (PacienteIn pacienteRequest, PasswordEncoder encoder){
        this.setNomePaciente(pacienteRequest.nomePaciente());
        this.setNomeConvenio(pacienteRequest.nomeConvenio());
        this.setCarteirinhaConvenio(pacienteRequest.carteirinhaConvenio());
        this.setUsuario(Usuario.builder()
                .username(pacienteRequest.username())
                .password(encoder.encode(pacienteRequest.password()))
                .tipoUsuario(TipoUsuario.PACIENTE)
                .build());
    }
}
