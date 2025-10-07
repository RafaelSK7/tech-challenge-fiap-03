package fiap.tech.challenge.hospital_manager.service.paciente;

import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.CreatePacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.DeletePacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.ReadPacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.UpdatePacienteUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;
import fiap.tech.challenge.hospital_manager.presenter.paciente.PacientePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    private ReadPacienteUseCase readUseCase;

    @Mock
    private CreatePacienteUseCase createUseCase;

    @Mock
    private UpdatePacienteUseCase updateUseCase;

    @Mock
    private DeletePacienteUseCase deleteUseCase;

    @InjectMocks
    private PacienteService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPaginaDePacientes_QuandoFindAllChamado() {
        // Arrange
        var pageRequest = PageRequest.of(0, 10);
        var pacienteDomain = mock(Paciente.class); // substitua se tiver um domain model real
        var pageDomain = new PageImpl<>(List.of(pacienteDomain));
        when(readUseCase.findAll(pageRequest)).thenReturn(pageDomain);

        // Mock do Presenter estático
        try (var mockedPresenter = mockStatic(PacientePresenter.class)) {
            var expectedOut = mock(PacienteOut.class);
            mockedPresenter.when(() -> PacientePresenter.toResponse(pacienteDomain))
                    .thenReturn(expectedOut);

            // Act
            Page<PacienteOut> result = service.findAll(pageRequest);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(expectedOut);
            verify(readUseCase, times(1)).findAll(pageRequest);
        }
    }

    @Test
    void deveRetornarPacientePorId_QuandoFindByIdChamado() {
        // Arrange
        var id = 1L;
        var pacienteDomain = mock(Paciente.class);
        when(readUseCase.findById(id)).thenReturn(pacienteDomain);

        try (var mockedPresenter = mockStatic(PacientePresenter.class)) {
            var expectedOut = mock(PacienteOut.class);
            mockedPresenter.when(() -> PacientePresenter.toResponse(pacienteDomain))
                    .thenReturn(expectedOut);

            // Act
            var result = service.findById(id);

            // Assert
            assertThat(result).isEqualTo(expectedOut);
            verify(readUseCase).findById(id);
        }
    }

    @Test
    void deveCriarPaciente_QuandoCreateChamado() {
        // Arrange
        var pacienteIn = mock(PacienteIn.class);
        var pacienteDomain = mock(Paciente.class);

        when(createUseCase.createPaciente(pacienteIn)).thenReturn(pacienteDomain);

        try (var mockedPresenter = mockStatic(PacientePresenter.class)) {
            var expectedOut = mock(PacienteOut.class);
            mockedPresenter.when(() -> PacientePresenter.toResponse(pacienteDomain))
                    .thenReturn(expectedOut);

            // Act
            var result = service.createPaciente(pacienteIn);

            // Assert
            assertThat(result).isEqualTo(expectedOut);
            verify(createUseCase).createPaciente(pacienteIn);
        }
    }

    @Test
    void deveAtualizarPaciente_QuandoUpdateChamado() {
        // Arrange
        var id = 10L;
        var pacienteIn = mock(PacienteIn.class);
        var pacienteDomain = mock(Paciente.class);

        when(updateUseCase.updatePaciente(pacienteIn, id)).thenReturn(pacienteDomain);

        try (var mockedPresenter = mockStatic(PacientePresenter.class)) {
            var expectedOut = mock(PacienteOut.class);
            mockedPresenter.when(() -> PacientePresenter.toResponse(pacienteDomain))
                    .thenReturn(expectedOut);

            // Act
            var result = service.updatePaciente(pacienteIn, id);

            // Assert
            assertThat(result).isEqualTo(expectedOut);
            verify(updateUseCase).updatePaciente(pacienteIn, id);
        }
    }

    @Test
    void deveDeletarPaciente_QuandoDeleteChamado() {
        // Arrange
        var id = 5L;

        // Act
        service.deletePaciente(id);

        // Assert
        verify(deleteUseCase, times(1)).deletePaciente(id);
    }
}
