package fiap.tech.challenge.hospital_manager.service.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.DesmarcarConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.ReadConsultaUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.consulta.UpdateConsultaUseCase;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConsultaServiceTest {

    @Mock
    private ReadConsultaUseCase readUseCase;

    @Mock
    private UpdateConsultaUseCase updateUseCase;

    @Mock
    private DesmarcarConsultaUseCase desmarcarUseCase;

    @InjectMocks
    private ConsultaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarListaDeConsultas_QuandoFindAllChamado() {
        // Arrange
        var consulta1 = mock(Consulta.class);
        var consulta2 = mock(Consulta.class);
        when(readUseCase.findAll()).thenReturn(List.of(consulta1, consulta2));

        // Act
        List<Consulta> result = service.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(consulta1, consulta2);
        verify(readUseCase, times(1)).findAll();
    }

    @Test
    void deveRetornarConsultaPorId_QuandoFindByIdChamado() {
        // Arrange
        var id = 1L;
        var expectedConsulta = mock(Consulta.class);
        when(readUseCase.findById(id)).thenReturn(expectedConsulta);

        // Act
        Consulta result = service.findById(id);

        // Assert
        assertThat(result).isEqualTo(expectedConsulta);
        verify(readUseCase, times(1)).findById(id);
    }

    @Test
    void deveAtualizarConsulta_QuandoUpdateChamado() {
        // Arrange
        var id = 5L;
        var consultaIn = mock(ConsultaIn.class);
        var expectedConsulta = mock(Consulta.class);
        when(updateUseCase.updateConsulta(consultaIn, id)).thenReturn(expectedConsulta);

        // Act
        Consulta result = service.updateConsulta(consultaIn, id);

        // Assert
        assertThat(result).isEqualTo(expectedConsulta);
        verify(updateUseCase, times(1)).updateConsulta(consultaIn, id);
    }

    @Test
    void deveDesmarcarConsulta_QuandoDeleteChamado() {
        // Arrange
        var id = 99L;
        when(desmarcarUseCase.deleteConsulta(id)).thenReturn(true);

        // Act
        Boolean result = service.desmarcarConsulta(id);

        // Assert
        assertThat(result).isTrue();
        verify(desmarcarUseCase, times(1)).deleteConsulta(id);
    }
}
