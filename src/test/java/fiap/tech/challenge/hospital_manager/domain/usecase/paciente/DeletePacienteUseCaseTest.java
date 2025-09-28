package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do DeletePacienteUseCase")
class DeletePacienteUseCaseTest {

    @Mock
    private ReadPacienteUseCase readPacienteUseCase;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private DeletePacienteUseCase deletePacienteUseCase;

    private Paciente paciente;
    private final Long PACIENTE_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        paciente = mock(Paciente.class);
        when(paciente.getIdPaciente()).thenReturn(PACIENTE_ID);
    }

    @Test
    @DisplayName("Deve deletar um paciente com sucesso")
    void shouldDeletePacienteSuccessfully() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID)).thenReturn(paciente);
        doNothing().when(pacienteRepository).delete(paciente);

        // Act
        deletePacienteUseCase.deletePaciente(PACIENTE_ID);

        // Assert
        verify(readPacienteUseCase, times(1)).findById(PACIENTE_ID);
        verify(pacienteRepository, times(1)).delete(paciente);
    }

    @Test
    @DisplayName("Deve propagar exceção quando paciente não for encontrado")
    void shouldPropagateExceptionWhenPacienteNotFound() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID))
            .thenThrow(new RuntimeException("Paciente não encontrado"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
            () -> deletePacienteUseCase.deletePaciente(PACIENTE_ID));
        assertEquals("Paciente não encontrado", exception.getMessage());
        verify(pacienteRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve propagar exceção quando houver erro ao deletar")
    void shouldPropagateExceptionWhenDeleteFails() {
        // Arrange
        when(readPacienteUseCase.findById(PACIENTE_ID)).thenReturn(paciente);
        doThrow(new RuntimeException("Erro ao deletar paciente"))
            .when(pacienteRepository).delete(paciente);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
            () -> deletePacienteUseCase.deletePaciente(PACIENTE_ID));
        assertEquals("Erro ao deletar paciente", exception.getMessage());
        verify(readPacienteUseCase, times(1)).findById(PACIENTE_ID);
    }
}
