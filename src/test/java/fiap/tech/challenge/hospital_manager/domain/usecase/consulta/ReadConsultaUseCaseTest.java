package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.exception.custom.ConsultaNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadConsultaUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private ReadConsultaUseCase readConsultaUseCase;

    private Consulta consulta;

    @BeforeEach
    void setUp() {
        consulta = new Consulta();
        consulta.setIdConsulta(1L);
    }

    @Test
    void deveRetornarTodasConsultas() {
        // Arrange
        when(consultaRepository.findAll()).thenReturn(List.of(consulta));

        // Act
        List<Consulta> resultado = readConsultaUseCase.findAll();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdConsulta());
        verify(consultaRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarConsultaPorIdQuandoExistente() {
        // Arrange
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        // Act
        Consulta resultado = readConsultaUseCase.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConsulta());
        verify(consultaRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoConsultaNaoEncontrada() {
        // Arrange
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ConsultaNotFoundException exception = assertThrows(
                ConsultaNotFoundException.class,
                () -> readConsultaUseCase.findById(1L)
        );

        assertEquals("Consulta com id: 1 nao encontrada", exception.getMessage());
        verify(consultaRepository, times(1)).findById(1L);
    }
}
