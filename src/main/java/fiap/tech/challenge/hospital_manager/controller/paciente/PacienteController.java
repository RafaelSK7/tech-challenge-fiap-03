package fiap.tech.challenge.hospital_manager.controller.paciente;

import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;
import fiap.tech.challenge.hospital_manager.service.paciente.PacienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
@Slf4j
public class PacienteController {

    private PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public Page<PacienteOut> findAll(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC, sort = "idPaciente") Pageable page) {
        log.info("Buscando todos os usuarios.");
        return pacienteService.findAll(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteOut> findById(@PathVariable Long id) {
        log.info("Buscando o usuario.");
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PacienteOut> findById(@RequestBody PacienteIn pacienteRequest,
                                                UriComponentsBuilder uriBuilder) {
        PacienteOut pacienteOut = pacienteService.createPaciente(pacienteRequest);
        URI uri = uriBuilder.path("/paciente/{id}").buildAndExpand(pacienteOut.pacienteId()).toUri();
        return ResponseEntity.created(uri).body(pacienteOut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteOut> creditarSaldo(@PathVariable Long id, @RequestBody PacienteIn pacienteRequest,
                                                       UriComponentsBuilder uriBuilder) {
        log.info("Cadastrando a conta.");
        return ResponseEntity.ok(pacienteService.updatePaciente(pacienteRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }
}
