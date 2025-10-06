package fiap.tech.challenge.hospital_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
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
@Entity(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfissional;

    @NotBlank
    private String nomeProfissional;

    private List<Especialidade> especialidades;

    private Integer conselhoRegional;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Consulta> consultas;

    // Ligação 1:1 com Usuario
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario", nullable = false)
    private Usuario usuario;

    public Profissional(ProfissionalIn profissionalIn, PasswordEncoder encoder) {
        this.setNomeProfissional(profissionalIn.nomeProfissional());
        this.setEspecialidades(profissionalIn.especialidades());
        this.setConselhoRegional(profissionalIn.conselhoRegional());
        this.setUsuario(Usuario.builder()
                .username(profissionalIn.username())
                .password(encoder.encode(profissionalIn.password()))
                .tipoUsuario(getEspecialidades().contains(Especialidade.ENFERMAGEM) ? TipoUsuario.ENFERMEIRO : TipoUsuario.MEDICO)
                .build());
    }

}
