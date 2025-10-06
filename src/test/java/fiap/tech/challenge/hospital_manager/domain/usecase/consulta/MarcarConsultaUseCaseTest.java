package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.ReadPacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.ReadProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.exception.custom.EspecialidadeNotFoundException;
import fiap.tech.challenge.hospital_manager.exception.custom.PacienteNotFoundException;
import fiap.tech.challenge.hospital_manager.exception.custom.ProfissionalNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes do MarcarConsultaUseCase")
class MarcarConsultaUseCaseTest {

    @Mock
    private ReadPacienteUseCase readPacienteUseCase;

    @Mock
    private ReadProfissionalUseCase readProfissionalUseCase;

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private MarcarConsultaUseCase marcarConsultaUseCase;

    private ConsultaIn consultaIn;
    private Paciente paciente;
    private Profissional profissional;
    private LocalDateTime dataHoraConsulta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dataHoraConsulta = LocalDateTime.now().plusDays(1);

        // Mock do Paciente
        paciente = mock(Paciente.class);
        when(paciente.getIdPaciente()).thenReturn(1L);
        when(paciente.getNomePaciente()).thenReturn("João Silva");

        // Mock do Profissional com uma especialidade
        profissional = mock(Profissional.class);
        when(profissional.getIdProfissional()).thenReturn(1L);
        when(profissional.getNomeProfissional()).thenReturn("Dr. José Santos");
        when(profissional.getEspecialidades()).thenReturn(List.of(Especialidade.CARDIOLOGIA));

        // Mock do ConsultaIn
        consultaIn = mock(ConsultaIn.class);
        when(consultaIn.idPaciente()).thenReturn(1L);
        when(consultaIn.idProfissional()).thenReturn(1L);
        when(consultaIn.especialidade()).thenReturn("CARDIOLOGIA");
        when(consultaIn.dataHoraConsulta()).thenReturn(dataHoraConsulta);
    }

    @Test
    @DisplayName("Deve marcar consulta com sucesso quando todos os dados estão corretos")
    void testMarcarConsultaSuccess() {
        // Given
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenReturn(profissional);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(new Consulta());

        // When/Then
        assertDoesNotThrow(() -> marcarConsultaUseCase.marcarConsulta(consultaIn));

        // Verify
        verify(readPacienteUseCase).findById(1L);
        verify(readProfissionalUseCase).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve marcar consulta com sucesso quando profissional tem múltiplas especialidades")
    void testMarcarConsultaSuccessMultiplasEspecialidades() {
        // Given
        when(profissional.getEspecialidades()).thenReturn(Arrays.asList(
            Especialidade.CARDIOLOGIA,
            Especialidade.NEUROLOGIA
        ));
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenReturn(profissional);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(new Consulta());

        // When/Then
        assertDoesNotThrow(() -> marcarConsultaUseCase.marcarConsulta(consultaIn));

        // Verify
        verify(readPacienteUseCase).findById(1L);
        verify(readProfissionalUseCase).findById(1L);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando paciente não é encontrado")
    void testMarcarConsultaPacienteNaoEncontrado() {
        // Given
        when(readPacienteUseCase.findById(1L)).thenThrow(new PacienteNotFoundException(1L));

        // When/Then
        assertThrows(PacienteNotFoundException.class, () ->
            marcarConsultaUseCase.marcarConsulta(consultaIn)
        );

        verify(readPacienteUseCase).findById(1L);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando profissional não é encontrado")
    void testMarcarConsultaProfissionalNaoEncontrado() {
        // Given
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenThrow(new ProfissionalNotFoundException(1L));

        // When/Then
        assertThrows(ProfissionalNotFoundException.class, () ->
            marcarConsultaUseCase.marcarConsulta(consultaIn)
        );

        verify(readPacienteUseCase).findById(1L);
        verify(readProfissionalUseCase).findById(1L);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando especialidade não existe")
    void testMarcarConsultaEspecialidadeInvalida() {
        // Given
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenReturn(profissional);
        when(consultaIn.especialidade()).thenReturn("ESPECIALIDADE_INVALIDA");

        // When/Then
        assertThrows(EspecialidadeNotFoundException.class, () ->
            marcarConsultaUseCase.marcarConsulta(consultaIn)
        );

        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando profissional não possui a especialidade")
    void testMarcarConsultaProfissionalSemEspecialidade() {
        // Given
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenReturn(profissional);
        when(consultaIn.especialidade()).thenReturn("NEUROLOGIA");
        when(profissional.getEspecialidades()).thenReturn(Collections.singletonList(Especialidade.CARDIOLOGIA));

        // When/Then
        assertThrows(EspecialidadeNotFoundException.class, () ->
            marcarConsultaUseCase.marcarConsulta(consultaIn)
        );

        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar consulta com data futura")
    void testMarcarConsultaDataFutura() {
        // Given
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(7);
        when(consultaIn.dataHoraConsulta()).thenReturn(dataFutura);
        when(readPacienteUseCase.findById(1L)).thenReturn(paciente);
        when(readProfissionalUseCase.findById(1L)).thenReturn(profissional);

        // When/Then
        assertDoesNotThrow(() -> marcarConsultaUseCase.marcarConsulta(consultaIn));
        verify(consultaRepository).save(any());
    }
}
