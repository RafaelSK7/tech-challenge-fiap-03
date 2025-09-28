package fiap.tech.challenge.hospital_manager.controller.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.domain.entity.Paciente;
import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import fiap.tech.challenge.hospital_manager.repository.paciente.PacienteRepository;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConsultaController {

    private PacienteRepository pacienteRepository;
    private ProfissionalRepository profissionalRepository;
    private ConsultaRepository consultaRepository;

    public ConsultaController(PacienteRepository pacienteRepository, ProfissionalRepository profissionalRepository, ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.consultaRepository = consultaRepository;
    }

    @QueryMapping
    public List<Consulta> todasConsultas() {
        return consultaRepository.findAll();
    }

    @QueryMapping
    public Consulta consultaPorId(@Argument Long id) {
        return consultaRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Paciente> pacientes() {
        return pacienteRepository.findAll();
    }

    @QueryMapping
    public List<Profissional> profissionais() {
        return profissionalRepository.findAll();
    }
}
