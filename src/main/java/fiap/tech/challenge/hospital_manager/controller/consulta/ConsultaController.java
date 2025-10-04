package fiap.tech.challenge.hospital_manager.controller.consulta;

import fiap.tech.challenge.hospital_manager.domain.entity.Consulta;
import fiap.tech.challenge.hospital_manager.dto.in.ConsultaIn;
import fiap.tech.challenge.hospital_manager.repository.consulta.ConsultaRepository;
import fiap.tech.challenge.hospital_manager.service.consulta.ConsultaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
    public List<Consulta> todasConsultas() {
        return consultaService.findAll();
    }

    @QueryMapping
    public Consulta consultaPorId(@Argument Long id) {
        return consultaService.findById(id);
    }

    @MutationMapping
    public Consulta atualizarConsulta(@Argument Long id, @Argument ConsultaIn consultaIn) {
        return consultaService.updateConsulta(consultaIn, id);
    }

    @MutationMapping
    public Boolean deletarConsulta(@Argument Long id) {
        return consultaService.desmarcarConsulta(id);
    }
}
