package fiap.tech.challenge.hospital_manager.domain.usecase.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.exception.custom.PacienteNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do ReadPacienteUseCase")
class ReadPacienteUseCaseTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ReadPacienteUseCase readPacienteUseCase;

    private Paciente paciente1;
    private Paciente paciente2;
    private final Long PACIENTE_ID = 1L;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        paciente1 = mock(Paciente.class);
        paciente2 = mock(Paciente.class);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar uma página de pacientes com sucesso")
    void shouldReturnPageOfPacientesSuccessfully() {
        // Arrange
        List<Paciente> pacientes = List.of(paciente1, paciente2);
        Page<Paciente> pacientePage = new PageImpl<>(pacientes);
        when(pacienteRepository.findAll(pageable)).thenReturn(pacientePage);

        // Act
        Page<Paciente> result = readPacienteUseCase.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(paciente1));
        assertTrue(result.getContent().contains(paciente2));
        verify(pacienteRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando não houver pacientes")
    void shouldReturnEmptyPageWhenNoPacientes() {
        // Arrange
        Page<Paciente> emptyPage = new PageImpl<>(List.of());
        when(pacienteRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<Paciente> result = readPacienteUseCase.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pacienteRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar um paciente por ID com sucesso")
    void shouldFindPacienteByIdSuccessfully() {
        // Arrange
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.of(paciente1));

        // Act
        Paciente result = readPacienteUseCase.findById(PACIENTE_ID);

        // Assert
        assertNotNull(result);
        assertSame(paciente1, result);
        verify(pacienteRepository, times(1)).findById(PACIENTE_ID);
    }

    @Test
    @DisplayName("Deve lançar PacienteNotFoundException quando paciente não for encontrado")
    void shouldThrowPacienteNotFoundExceptionWhenPacienteNotFound() {
        // Arrange
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PacienteNotFoundException.class,
            () -> readPacienteUseCase.findById(PACIENTE_ID));
        verify(pacienteRepository, times(1)).findById(PACIENTE_ID);
    }
}
