package fiap.tech.challenge.hospital_manager.controller.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Especialidade;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.dto.out.ProfissionalOut;
import fiap.tech.challenge.hospital_manager.service.profissional.ProfissionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfissionalControllerTest {

    @Mock
    private ProfissionalService profissionalService;

    @InjectMocks
    private ProfissionalController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void deveRetornarProfissionalPorId_QuandoGETByIdChamado() throws Exception {
        // Arrange
        ProfissionalOut profissionalOut = new ProfissionalOut(1L, "Maria", List.of(Especialidade.PEDIATRIA), 12345);
        when(profissionalService.findById(1L)).thenReturn(profissionalOut);

        // Act & Assert
        mockMvc.perform(get("/profissional/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(profissionalService).findById(1L);
    }

    @Test
    void deveCriarProfissional_QuandoPOSTChamado() throws Exception {
        // Arrange
        ProfissionalIn profissionalIn = new ProfissionalIn("Carlos", List.of(Especialidade.CLINICA_GERAL), 12345, "username", "guest");
        ProfissionalOut profissionalOut = new ProfissionalOut(10L, "Carlos", List.of(Especialidade.CIRURGIA_GERAL), 12345);

        when(profissionalService.createProfissional(any())).thenReturn(profissionalOut);

        String jsonRequest = """
                {
                  "nome": "Carlos",
                  "especialidade": "CIRURGIA_GERAL"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/profissional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(profissionalService).createProfissional(any());
    }

    @Test
    void deveAtualizarProfissional_QuandoPUTChamado() throws Exception {
        // Arrange
        ProfissionalIn profissionalIn = new ProfissionalIn("Lucas", List.of(Especialidade.PEDIATRIA), 12345, "username", "guest");
        ProfissionalOut profissionalOut = new ProfissionalOut(5L, "Lucas", List.of(Especialidade.PEDIATRIA), 12345);

        when(profissionalService.updateProfissional(any(), eq(5L)))
                .thenReturn(profissionalOut);

        String jsonRequest = """
                {
                  "nome": "Lucas",
                  "especialidade": "PEDIATRIA"
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/profissional/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(profissionalService).updateProfissional(any(), eq(5L));
    }

    @Test
    void deveDeletarProfissional_QuandoDELETEChamado() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/profissional/7"))
                .andExpect(status().isNoContent());

        verify(profissionalService).deleteProfissional(7L);
    }
}
