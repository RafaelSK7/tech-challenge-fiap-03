package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do CreatePacienteUseCase")
class CreatePacienteUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private CreatePacienteUseCase createPacienteUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    private PacienteIn pacienteIn;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        pacienteIn = new PacienteIn(
            "João Silva",
            "Unimed",
            "123456789",
                "paciente",
                "guest"
        );

        paciente = new Paciente(pacienteIn, new BCryptPasswordEncoder());
    }

    @Test
    @DisplayName("Deve criar um paciente com sucesso")
    void shouldCreatePacienteSuccessfully() {
        // Arrange
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(passwordEncoder.encode(any())).thenReturn("encoded password");

        // Act
        Paciente createdPaciente = createPacienteUseCase.createPaciente(pacienteIn);

        // Assert
        assertNotNull(createdPaciente);
        assertEquals(pacienteIn.nomePaciente(), createdPaciente.getNomePaciente());
        assertEquals(pacienteIn.nomeConvenio(), createdPaciente.getNomeConvenio());
        assertEquals(pacienteIn.carteirinhaConvenio(), createdPaciente.getCarteirinhaConvenio());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando repository falhar")
    void shouldThrowExceptionWhenRepositoryFails() {
        // Arrange
        when(pacienteRepository.save(any(Paciente.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> createPacienteUseCase.createPaciente(pacienteIn));
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
}
