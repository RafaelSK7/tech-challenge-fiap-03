package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.exception.custom.ProfissionalNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do DeleteProfissionalUseCase")
class DeleteProfissionalUseCaseTest {

    @Mock
    private ReadProfissionalUseCase readProfissionalUseCase;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private DeleteProfissionalUseCase deleteProfissionalUseCase;

    private Profissional profissional;
    private final Long PROFISSIONAL_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        profissional = mock(Profissional.class);
    }

    @Test
    @DisplayName("Deve deletar um profissional com sucesso")
    void shouldDeleteProfissionalSuccessfully() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID)).thenReturn(profissional);
        doNothing().when(profissionalRepository).delete(profissional);

        // Act
        deleteProfissionalUseCase.deleteProfissional(PROFISSIONAL_ID);

        // Assert
        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, times(1)).delete(profissional);
    }

    @Test
    @DisplayName("Deve propagar exceção quando profissional não for encontrado")
    void shouldPropagateExceptionWhenProfissionalNotFound() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID))
            .thenThrow(new ProfissionalNotFoundException(PROFISSIONAL_ID));

        // Act & Assert
        assertThrows(ProfissionalNotFoundException.class,
            () -> deleteProfissionalUseCase.deleteProfissional(PROFISSIONAL_ID));

        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve propagar exceção quando houver erro ao deletar")
    void shouldPropagateExceptionWhenDeleteFails() {
        // Arrange
        when(readProfissionalUseCase.findById(PROFISSIONAL_ID)).thenReturn(profissional);
        doThrow(new RuntimeException("Erro ao deletar profissional"))
            .when(profissionalRepository).delete(profissional);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> deleteProfissionalUseCase.deleteProfissional(PROFISSIONAL_ID));
        assertEquals("Erro ao deletar profissional", exception.getMessage());

        verify(readProfissionalUseCase, times(1)).findById(PROFISSIONAL_ID);
        verify(profissionalRepository, times(1)).delete(profissional);
    }
}
