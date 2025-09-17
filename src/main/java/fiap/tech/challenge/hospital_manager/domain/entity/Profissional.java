package fiap.tech.challenge.hospital_manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name="profissional")
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


}
