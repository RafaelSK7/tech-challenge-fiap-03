package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.ReadPacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.ReadProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.exception.custom.EspecialidadeNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
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
class UpdateConsultaUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private ReadConsultaUseCase readConsultaUseCase;

    @Mock
    private ReadPacienteUseCase readPacienteUseCase;

    @Mock
    private ReadProfissionalUseCase readProfissionalUseCase;

    @InjectMocks
    private UpdateConsultaUseCase updateConsultaUseCase;

    private ConsultaIn consultaIn;
    private Consulta consultaExistente;
    private Paciente paciente;
    private Profissional profissional;

    @BeforeEach
    void setUp() {
        consultaIn = new ConsultaIn(
                LocalDateTime.now().plusDays(1), // dataHoraConsulta
                1L, // idPaciente
                2L, // idProfissional
                "ginecologia" // especialidade

        );

        consultaExistente = new Consulta();
        consultaExistente.setIdConsulta(99L);

        paciente = new Paciente();
        paciente.setIdPaciente(1L);

        profissional = new Profissional();
        profissional.setIdProfissional(2L);
        profissional.setEspecialidades(List.of(Especialidade.GINECOLOGIA_OBSTETRICIA));
    }

    @Test
    void deveAtualizarConsultaComSucesso() {
        // Arrange
        when(readConsultaUseCase.findById(99L)).thenReturn(consultaExistente);
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(2L)).thenReturn(profissional);
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Consulta resultado = updateConsultaUseCase.updateConsulta(consultaIn, 99L);

        // Assert
        assertNotNull(resultado);
        assertEquals(Especialidade.GINECOLOGIA_OBSTETRICIA, resultado.getEspecialidade());
        assertEquals(paciente, resultado.getPaciente());
        assertEquals(profissional, resultado.getProfissional());
        assertEquals(consultaIn.dataHoraConsulta(), resultado.getDataHoraConsulta());

        verify(readConsultaUseCase).findById(99L);
        verify(readPacienteUseCase).findById(1L);
        verify(readProfissionalUseCase).findById(2L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfissionalNaoTemEspecialidade() {
        // Arrange
        profissional.setEspecialidades(List.of(Especialidade.PEDIATRIA)); // diferente da consulta
        when(readConsultaUseCase.findById(99L)).thenReturn(consultaExistente);
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(2L)).thenReturn(profissional);

        // Act & Assert
        assertThrows(EspecialidadeNotFoundException.class,
                () -> updateConsultaUseCase.updateConsulta(consultaIn, 99L));

        verify(consultaRepository, never()).save(any());
    }
}
