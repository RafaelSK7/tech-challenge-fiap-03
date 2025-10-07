package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.exception.custom.PacienteNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do UpdatePacienteUseCase")
class UpdatePacienteUseCaseTest {

    @Mock
    private ReadPacienteUseCase readPacienteUseCase;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private UpdatePacienteUseCase updatePacienteUseCase;

    private Paciente pacienteExistente;
    private PacienteIn pacienteRequest;
    private final Long PACIENTE_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        pacienteExistente = new Paciente(
            new PacienteIn("Nome Antigo", "Convenio Antigo", "Carteirinha Antiga","paciente", "guest"), new BCryptPasswordEncoder()
        );

        pacienteRequest = new PacienteIn(
            "Nome Novo",
            "Convenio Novo",
            "Carteirinha Nova",
                "paciente",
                "guest"
        );
    }

    @Test
    @DisplayName("Deve atualizar um paciente com sucesso")
    void shouldUpdatePacienteSuccessfully() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID)).thenReturn(pacienteExistente);
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Paciente result = updatePacienteUseCase.updatePaciente(pacienteRequest, PACIENTE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(pacienteRequest.nomePaciente(), result.getNomePaciente());
        assertEquals(pacienteRequest.nomeConvenio(), result.getNomeConvenio());
        assertEquals(pacienteRequest.carteirinhaConvenio(), result.getCarteirinhaConvenio());

        verify(readPacienteUseCase, times(1)).findById(PACIENTE_ID);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve propagar PacienteNotFoundException quando paciente não existir")
    void shouldPropagatePacienteNotFoundExceptionWhenPacienteDoesNotExist() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID))
            .thenThrow(new PacienteNotFoundException(PACIENTE_ID));

        // Act & Assert
        assertThrows(PacienteNotFoundException.class,
            () -> updatePacienteUseCase.updatePaciente(pacienteRequest, PACIENTE_ID));

        verify(readPacienteUseCase, times(1)).findById(PACIENTE_ID);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve propagar exceção quando houver erro ao salvar")
    void shouldPropagateExceptionWhenSaveFails() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID)).thenReturn(pacienteExistente);
        when(pacienteRepository.save(any(Paciente.class)))
            .thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
            () -> updatePacienteUseCase.updatePaciente(pacienteRequest, PACIENTE_ID));
        assertEquals("Erro ao salvar", exception.getMessage());

        verify(readPacienteUseCase, times(1)).findById(PACIENTE_ID);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
}
