package fiap.tech.challenge.hospital_manager.domain.usecase.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.domain.usecase.paciente.ReadPacienteUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.ReadProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.ConsultaIn;
import fiap.tech.challenge.hospital_manager.exception.custom.EspecialidadeNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateConsultaUseCase {

    private ConsultaRepository consultaRepository;
    private ReadConsultaUseCase readConsultaUseCase;
    private ReadPacienteUseCase readPacienteUseCase;
    private ReadProfissionalUseCase readProfissionalUseCase;

    public UpdateConsultaUseCase(ConsultaRepository consultaRepository, ReadConsultaUseCase readConsultaUseCase) {
        this.consultaRepository = consultaRepository;
        this.readConsultaUseCase = readConsultaUseCase;
    }

    public Consulta updateConsulta(ConsultaIn consultaIn, Long idConsulta) {
        Consulta updatedConsulta = readConsultaUseCase.findById(idConsulta);
        Paciente paciente = readPacienteUseCase.findById(consultaIn.idPaciente());
        Profissional profissional = readProfissionalUseCase.findById(consultaIn.idProfissional());

        Especialidade especialidadeConsulta = Especialidade.fromDescricao(consultaIn.especialidade());

        if (!profissional.getEspecialidades().contains(especialidadeConsulta))
            throw new EspecialidadeNotFoundException();

        updatedConsulta.setEspecialidade(especialidadeConsulta);
        updatedConsulta.setDataHoraConsulta(consultaIn.dataHoraConsulta());
        updatedConsulta.setPaciente(paciente);
        updatedConsulta.setProfissional(profissional);

        return consultaRepository.save(updatedConsulta);
    }
}
