package fiap.tech.challenge.hospital_manager.controller.profissional;

import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;
import fiap.tech.challenge.hospital_manager.dto.out.ProfissionalOut;
import fiap.tech.challenge.hospital_manager.service.profissional.ProfissionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/profissional")
@Slf4j
public class ProfissionalController {

    private ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public Page<ProfissionalOut> findAll(
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC, sort = "idProfissional") Pageable page) {
        log.info("Buscando todos os usuarios.");
        return profissionalService.findAll(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalOut> findById(@PathVariable Long id) {
        log.info("Buscando o usuario.");
        return ResponseEntity.ok(profissionalService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProfissionalOut> createPaciente(@RequestBody ProfissionalIn profissionalIn,
                                                          UriComponentsBuilder uriBuilder) {
        ProfissionalOut profissionalOut = profissionalService.createProfissional(profissionalIn);
        URI uri = uriBuilder.path("/paciente/{id}").buildAndExpand(profissionalOut.idProfissional()).toUri();
        return ResponseEntity.created(uri).body(profissionalOut);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalOut> updatePaciente(@PathVariable Long id, @RequestBody ProfissionalIn profissionalIn,
                                                          UriComponentsBuilder uriBuilder) {
        log.info("Cadastrando a conta.");
        return ResponseEntity.ok(profissionalService.updateProfissional(profissionalIn, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        profissionalService.deleteProfissional(id);
        return ResponseEntity.noContent().build();
    }
}
