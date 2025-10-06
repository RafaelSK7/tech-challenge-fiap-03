package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do CreateProfissionalUseCase")
class CreateProfissionalUseCaseTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private CreateProfissionalUseCase createProfissionalUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ProfissionalIn profissionalIn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        profissionalIn = new ProfissionalIn(
            "Dr. João Silva",
            List.of(Especialidade.CARDIOLOGIA),
            123456,
            "profissional",
                "guest"
        );
    }

    @Test
    @DisplayName("Deve criar um profissional com sucesso")
    void shouldCreateProfissionalSuccessfully() {
        // Arrange
        when(profissionalRepository.save(any(Profissional.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("encoded password");
        // Act
        Profissional createdProfissional = createProfissionalUseCase.createProfissional(profissionalIn);

        // Assert
        assertNotNull(createdProfissional);
        assertEquals(profissionalIn.nomeProfissional(), createdProfissional.getNomeProfissional());
        assertEquals(profissionalIn.especialidades(), createdProfissional.getEspecialidades());
        assertEquals(profissionalIn.conselhoRegional(), createdProfissional.getConselhoRegional());
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve propagar exceção quando repository falhar")
    void shouldThrowExceptionWhenRepositoryFails() {
        // Arrange
        when(profissionalRepository.save(any(Profissional.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
            () -> createProfissionalUseCase.createProfissional(profissionalIn));
        assertEquals("Erro ao salvar", exception.getMessage());
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }
}
