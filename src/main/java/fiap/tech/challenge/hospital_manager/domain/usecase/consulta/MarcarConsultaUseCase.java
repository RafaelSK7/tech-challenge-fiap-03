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
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MarcarConsultaUseCase {

    private ReadPacienteUseCase readPacienteUseCase;

    private ReadProfissionalUseCase readProfissionalUseCase;

    private ConsultaRepository consultaRepository;

    public MarcarConsultaUseCase(ReadPacienteUseCase pacienteRepository, ReadProfissionalUseCase profissionalRepository, ConsultaRepository consultaRepository
    ) {
        this.readPacienteUseCase = pacienteRepository;
        this.readProfissionalUseCase = profissionalRepository;
        this.consultaRepository = consultaRepository;
    }


    public void marcarConsulta(ConsultaIn consultaIn) {

        Paciente paciente = readPacienteUseCase.findById(consultaIn.idPaciente());
        Profissional profissional = readProfissionalUseCase.findById(consultaIn.idProfissional());

        try {
            Especialidade especialidadeConsulta = Especialidade.fromDescricao(consultaIn.especialidade());
            if (!profissional.getEspecialidades().contains(especialidadeConsulta))
                throw new EspecialidadeNotFoundException();
            Consulta consulta = new Consulta(consultaIn.dataHoraConsulta(), paciente, profissional, especialidadeConsulta);
            consultaRepository.save(consulta);
        } catch (IllegalArgumentException e) {
            throw new EspecialidadeNotFoundException();
        }
    }
}
