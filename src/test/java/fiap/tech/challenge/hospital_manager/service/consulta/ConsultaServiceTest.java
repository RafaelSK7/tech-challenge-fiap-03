package fiap.tech.challenge.hospital_manager.service.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.DesmarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.ReadConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.UpdateConsultaUseCase;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ReadConsultaUseCase readConsultaUseCase;

    @Mock
    private UpdateConsultaUseCase updateConsultaUseCase;

    @Mock
    private DesmarcarConsultaUseCase desmarcarConsultaUseCase;

    @InjectMocks
    private ConsultaService consultaService;

    private Consulta consulta;
    private ConsultaIn consultaIn;

    @BeforeEach
    void setUp() {
        consulta = new Consulta();
        consulta.setIdConsulta(1L);

        consultaIn = new ConsultaIn(LocalDateTime.now(), 1l, 1l, "ginecologia");
        // se necessário, configurar campos de consultaIn
    }

    @Test
    void deveRetornarTodasConsultas() {
        // Arrange
        when(readConsultaUseCase.findAll()).thenReturn(List.of(consulta));

        // Act
        List<Consulta> resultado = consultaService.findAll();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdConsulta());
        verify(readConsultaUseCase, times(1)).findAll();
        verifyNoInteractions(updateConsultaUseCase, desmarcarConsultaUseCase);
    }

    @Test
    void deveRetornarConsultaPorId() {
        // Arrange
        when(readConsultaUseCase.findById(1L)).thenReturn(consulta);

        // Act
        Consulta resultado = consultaService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConsulta());
        verify(readConsultaUseCase, times(1)).findById(1L);
        verifyNoInteractions(updateConsultaUseCase, desmarcarConsultaUseCase);
    }

    @Test
    void deveAtualizarConsulta() {
        // Arrange
        when(updateConsultaUseCase.updateConsulta(consultaIn, 1L)).thenReturn(consulta);

        // Act
        Consulta resultado = consultaService.updateConsulta(consultaIn, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConsulta());
        verify(updateConsultaUseCase, times(1)).updateConsulta(consultaIn, 1L);
        verifyNoInteractions(readConsultaUseCase, desmarcarConsultaUseCase);
    }

    @Test
    void deveDesmarcarConsulta() {
        // Arrange
        when(desmarcarConsultaUseCase.deleteConsulta(1L)).thenReturn(true);

        // Act
        Boolean resultado = consultaService.desmarcarConsulta(1L);

        // Assert
        assertTrue(resultado);
        verify(desmarcarConsultaUseCase, times(1)).deleteConsulta(1L);
        verifyNoInteractions(readConsultaUseCase, updateConsultaUseCase);
    }
}
