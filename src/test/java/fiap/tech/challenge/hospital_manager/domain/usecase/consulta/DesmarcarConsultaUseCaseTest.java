package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesmarcarConsultaUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private ReadConsultaUseCase readConsultaUseCase;

    @InjectMocks
    private DesmarcarConsultaUseCase desmarcarConsultaUseCase;

    private Consulta consulta;

    @BeforeEach
    void setUp() {
        consulta = new Consulta();
        consulta.setIdConsulta(1L);
    }

    @Test
    void deveDeletarConsultaComSucesso() {
        // Arrange
        when(readConsultaUseCase.findById(1L)).thenReturn(consulta);
        doNothing().when(consultaRepository).delete(consulta);

        // Act
        Boolean resultado = desmarcarConsultaUseCase.deleteConsulta(1L);

        // Assert
        assertTrue(resultado);
        verify(readConsultaUseCase, times(1)).findById(1L);
        verify(consultaRepository, times(1)).delete(consulta);
    }

    @Test
    void deveRetornarFalseQuandoConsultaNaoEncontrada() {
        // Arrange
        when(readConsultaUseCase.findById(1L)).thenThrow(new RuntimeException("Consulta não encontrada"));

        // Act
        Boolean resultado = desmarcarConsultaUseCase.deleteConsulta(1L);

        // Assert
        assertFalse(resultado);
        verify(readConsultaUseCase, times(1)).findById(1L);
        verify(consultaRepository, never()).delete(any());
    }
}
