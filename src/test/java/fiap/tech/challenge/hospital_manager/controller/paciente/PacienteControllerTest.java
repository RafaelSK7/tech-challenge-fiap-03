package fiap.tech.challenge.hospital_manager.controller.paciente;

import fiap.tech.challenge.hospital_manager.dto.in.PacienteIn;
import fiap.tech.challenge.hospital_manager.dto.out.PacienteOut;
import fiap.tech.challenge.hospital_manager.service.paciente.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PacienteControllerTest {

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void deveRetornarpacientePorId_QuandoGETByIdChamado() throws Exception {
        // Arrange
        PacienteOut pacienteOut = new PacienteOut(1L, "Maria", "Amil");
        when(pacienteService.findById(1L)).thenReturn(pacienteOut);

        // Act & Assert
        mockMvc.perform(get("/pacientes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(pacienteService).findById(1L);
    }

    @Test
    void deveCriarpaciente_QuandoPOSTChamado() throws Exception {
        // Arrange
        PacienteIn pacienteIn = new PacienteIn("Carlos", "Notredame", "12345", "username", "guest");
        PacienteOut pacienteOut = new PacienteOut(10L, "Carlos", "Notredame");

        when(pacienteService.createPaciente(any())).thenReturn(pacienteOut);

        String jsonRequest = """
                {
                  "nomePaciente": "Carlos",
                  "nomeConvenio": "Notredame",
                  "carteirinhaConvenio": "12345",
                  "username": "username",
                  "password": "guest"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(pacienteService).createPaciente(any());
    }

    @Test
    void deveAtualizarpaciente_QuandoPUTChamado() throws Exception {
        // Arrange
        PacienteIn pacienteIn = new PacienteIn("Lucas", "Bradesco", "12345", "username", "guest");
        PacienteOut pacienteOut = new PacienteOut(5L, "Lucas", "Bradesco");

        when(pacienteService.updatePaciente(any(), eq(5L)))
                .thenReturn(pacienteOut);

        String jsonRequest = """
                {
                      "nomePaciente": "Lucas",
                      "nomeConvenio": "Bradesco",
                      "carteirinhaConvenio": "12345",
                      "username": "username",
                      "password": "guest"
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/pacientes/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(pacienteService).updatePaciente(any(), eq(5L));
    }

    @Test
    void deveDeletarpaciente_QuandoDELETEChamado() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/pacientes/7"))
                .andExpect(status().isNoContent());

        verify(pacienteService).deletePaciente(7L);
    }
}
