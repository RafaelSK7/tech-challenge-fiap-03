package fiap.tech.challenge.hospital_manager.controller.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import fiap.tech.challenge.hospital_manager.service.consulta.ConsultaService;
import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
public class ConsultaController {

    private ConsultaRepository consultaRepository;

    private ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO') or hasRole('PACIENTE')")
    public List<Consulta> todasConsultas() {
        return consultaService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO') or hasRole('PACIENTE')")
    public Consulta consultaPorId(@Argument Long id) {
        return consultaService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public Consulta atualizarConsulta(@Argument Long id, @Argument("input") ConsultaIn consultaIn) {
        return consultaService.updateConsulta(consultaIn, id);
    }

    @PreAuthorize("hasRole('MEDICO')")
    @MutationMapping
    public Boolean deletarConsulta(@Argument Long id) {
        return consultaService.desmarcarConsulta(id);
    }
}
