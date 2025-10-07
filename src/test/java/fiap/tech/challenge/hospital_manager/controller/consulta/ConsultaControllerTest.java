package fiap.tech.challenge.hospital_manager.controller.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.service.consulta.ConsultaService;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    private Consulta consulta;
    private ConsultaIn consultaIn;

    @BeforeEach
    void setUp() {
        consulta = new Consulta();
        consulta.setIdConsulta(1L);

        consultaIn = new ConsultaIn(LocalDateTime.now(), 1l, 1l, "ginecologia");
    }

    @Test
    void deveRetornarTodasConsultas() {
        // Arrange
        List<Consulta> consultas = Arrays.asList(consulta);
        when(consultaService.findAll()).thenReturn(consultas);

        // Act
        List<Consulta> resultado = consultaController.todasConsultas();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdConsulta());
        verify(consultaService, times(1)).findAll();
    }

    @Test
    void deveRetornarConsultaPorId() {
        // Arrange
        when(consultaService.findById(1L)).thenReturn(consulta);

        // Act
        Consulta resultado = consultaController.consultaPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConsulta());
        verify(consultaService, times(1)).findById(1L);
    }

    @Test
    void deveAtualizarConsulta() {
        // Arrange
        when(consultaService.updateConsulta(consultaIn, 1L)).thenReturn(consulta);

        // Act
        Consulta resultado = consultaController.atualizarConsulta(1L, consultaIn);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConsulta());
        verify(consultaService, times(1)).updateConsulta(consultaIn, 1L);
    }

    @Test
    void deveDeletarConsulta() {
        // Arrange
        when(consultaService.desmarcarConsulta(1L)).thenReturn(true);

        // Act
        Boolean resultado = consultaController.deletarConsulta(1L);

        // Assert
        assertTrue(resultado);
        verify(consultaService, times(1)).desmarcarConsulta(1L);
    }
}
