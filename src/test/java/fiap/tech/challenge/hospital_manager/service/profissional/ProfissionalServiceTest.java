package fiap.tech.challenge.hospital_manager.service.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.CreateProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.DeleteProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.ReadProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.domain.usecase.profissional.UpdateProfissionalUseCase;
import fiap.tech.challenge.hospital_manager.dto.in.ProfissionalIn;
import fiap.tech.challenge.hospital_manager.dto.out.ProfissionalOut;
import fiap.tech.challenge.hospital_manager.presenter.profissional.ProfissionalPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProfissionalServiceTest {

    @Mock
    private CreateProfissionalUseCase createUseCase;

    @Mock
    private ReadProfissionalUseCase readUseCase;

    @Mock
    private UpdateProfissionalUseCase updateUseCase;

    @Mock
    private DeleteProfissionalUseCase deleteUseCase;

    @InjectMocks
    private ProfissionalService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPaginaDeProfissionais_QuandoFindAllChamado() {
        // Arrange
        var pageRequest = PageRequest.of(0, 10);
        var profissionalDomain = mock(Profissional.class); // tipo real do domínio omitido
        var pageDomain = new PageImpl<>(List.of(profissionalDomain));

        when(readUseCase.findAll(pageRequest)).thenReturn(pageDomain);

        // Mock static do Presenter
        try (var mockedPresenter = mockStatic(ProfissionalPresenter.class)) {
            var expectedOut = mock(ProfissionalOut.class);
            mockedPresenter.when(() -> ProfissionalPresenter.toResponse(profissionalDomain))
                    .thenReturn(expectedOut);

            // Act
            Page<ProfissionalOut> result = service.findAll(pageRequest);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(expectedOut);
            verify(readUseCase, times(1)).findAll(pageRequest);
        }
    }

    @Test
    void deveRetornarProfissionalPorId_QuandoFindByIdChamado() {
        var id = 1L;
        var profissionalDomain = mock(Profissional.class);
        when(readUseCase.findById(id)).thenReturn(profissionalDomain);

        try (var mockedPresenter = mockStatic(ProfissionalPresenter.class)) {
            var expectedOut = mock(ProfissionalOut.class);
            mockedPresenter.when(() -> ProfissionalPresenter.toResponse(profissionalDomain))
                    .thenReturn(expectedOut);

            var result = service.findById(id);

            assertThat(result).isEqualTo(expectedOut);
            verify(readUseCase).findById(id);
        }
    }

    @Test
    void deveCriarProfissional_QuandoCreateChamado() {
        var profissionalIn = mock(ProfissionalIn.class);
        var profissionalDomain = mock(Profissional.class);

        when(createUseCase.createProfissional(profissionalIn)).thenReturn(profissionalDomain);

        try (var mockedPresenter = mockStatic(ProfissionalPresenter.class)) {
            var expectedOut = mock(ProfissionalOut.class);
            mockedPresenter.when(() -> ProfissionalPresenter.toResponse(profissionalDomain))
                    .thenReturn(expectedOut);

            var result = service.createProfissional(profissionalIn);

            assertThat(result).isEqualTo(expectedOut);
            verify(createUseCase).createProfissional(profissionalIn);
        }
    }

    @Test
    void deveAtualizarProfissional_QuandoUpdateChamado() {
        var id = 1L;
        var profissionalIn = mock(ProfissionalIn.class);
        var profissionalDomain = mock(Profissional.class);

        when(updateUseCase.updateProfissional(profissionalIn, id)).thenReturn(profissionalDomain);

        try (var mockedPresenter = mockStatic(ProfissionalPresenter.class)) {
            var expectedOut = mock(ProfissionalOut.class);
            mockedPresenter.when(() -> ProfissionalPresenter.toResponse(profissionalDomain))
                    .thenReturn(expectedOut);

            var result = service.updateProfissional(profissionalIn, id);

            assertThat(result).isEqualTo(expectedOut);
            verify(updateUseCase).updateProfissional(profissionalIn, id);
        }
    }

    @Test
    void deveDeletarProfissional_QuandoDeleteChamado() {
        var id = 99L;

        service.deleteProfissional(id);

        verify(deleteUseCase, times(1)).deleteProfissional(id);
    }
}
