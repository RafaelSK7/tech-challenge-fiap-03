package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.exception.custom.ProfissionalNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do UpdateProfissionalUseCase")
class UpdateProfissionalUseCaseTest {

    @Mock
    private ReadProfissionalUseCase readProfissionalUseCase;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private UpdateProfissionalUseCase updateProfissionalUseCase;

    private Profissional profissionalExistente;
    private ProfissionalIn profissionalRequest;
    private final Long PROFISSIONAL_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        profissionalExistente = new Profissional(
            new ProfissionalIn(
                "Dr. João Silva",
                List.of(Especialidade.CARDIOLOGIA),
                123456,
                    "profissional",
                    "guest"
            ), new BCryptPasswordEncoder()
        );

        profissionalRequest = new ProfissionalIn(
            "Dr. João Silva Atualizado",
            List.of(Especialidade.CARDIOLOGIA, Especialidade.CLINICA_GERAL),
            654321, "profissional", "guest"
        );
    }

    @Test
    @DisplayName("Deve atualizar um profissional com sucesso")
    void shouldUpdateProfissionalSuccessfully() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID)).thenReturn(profissionalExistente);
        when(profissionalRepository.save(any(Profissional.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Profissional result = updateProfissionalUseCase.updateProfissional(profissionalRequest, PROFISSIONAL_ID);

        // Assert
        assertNotNull(result);
        assertEquals(profissionalRequest.nomeProfissional(), result.getNomeProfissional());
        assertEquals(profissionalRequest.especialidades(), result.getEspecialidades());
        assertEquals(profissionalRequest.conselhoRegional(), result.getConselhoRegional());

        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve propagar ProfissionalNotFoundException quando profissional não existir")
    void shouldPropagateProfissionalNotFoundExceptionWhenProfissionalDoesNotExist() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID))
            .thenThrow(new ProfissionalNotFoundException(PROFISSIONAL_ID));

        // Act & Assert
        assertThrows(ProfissionalNotFoundException.class,
            () -> updateProfissionalUseCase.updateProfissional(profissionalRequest, PROFISSIONAL_ID));

        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

    @Test
    @DisplayName("Deve propagar exceção quando houver erro ao salvar")
    void shouldPropagateExceptionWhenSaveFails() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID)).thenReturn(profissionalExistente);
        when(profissionalRepository.save(any(Profissional.class)))
            .thenThrow(new RuntimeException("Erro ao salvar"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
            () -> updateProfissionalUseCase.updateProfissional(profissionalRequest, PROFISSIONAL_ID));
        assertEquals("Erro ao salvar", exception.getMessage());

        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
    }
}
