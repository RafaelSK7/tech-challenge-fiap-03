package fiap.tech.challenge.hospital_manager.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario; // PACIENTE, MEDICO, ENFERMEIRO.

    // Ligação 1:1 com Paciente (opcional)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Paciente paciente;

    // Ligação 1:1 com Profissional (opcional)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Profissional profissional;
}
