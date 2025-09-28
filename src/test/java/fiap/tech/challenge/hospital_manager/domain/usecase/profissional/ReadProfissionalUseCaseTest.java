package fiap.tech.challenge.hospital_manager.domain.usecase.profissional;

import fiap.tech.challenge.hospital_manager.domain.entity.Profissional;
import fiap.tech.challenge.hospital_manager.exception.custom.ProfissionalNotFoundException;
import fiap.tech.challenge.hospital_manager.repository.profissional.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes do ReadProfissionalUseCase")
class ReadProfissionalUseCaseTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private ReadProfissionalUseCase readProfissionalUseCase;

    private Profissional profissional1;
    private Profissional profissional2;
    private final Long PROFISSIONAL_ID = 1L;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        profissional1 = mock(Profissional.class);
        profissional2 = mock(Profissional.class);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar uma página de profissionais com sucesso")
    void shouldReturnPageOfProfissionaisSuccessfully() {
        // Arrange
        List<Profissional> profissionais = List.of(profissional1, profissional2);
        Page<Profissional> profissionalPage = new PageImpl<>(profissionais);
        when(profissionalRepository.findAll(pageable)).thenReturn(profissionalPage);

        // Act
        Page<Profissional> result = readProfissionalUseCase.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(profissional1));
        assertTrue(result.getContent().contains(profissional2));
        verify(profissionalRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando não houver profissionais")
    void shouldReturnEmptyPageWhenNoProfissionais() {
        // Arrange
        Page<Profissional> emptyPage = new PageImpl<>(List.of());
        when(profissionalRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<Profissional> result = readProfissionalUseCase.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(profissionalRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar um profissional por ID com sucesso")
    void shouldFindProfissionalByIdSuccessfully() {
        // Arrange
        when(profissionalRepository.findById(PROFISSIONAL_ID)).thenReturn(Optional.of(profissional1));

        // Act
        Profissional result = readProfissionalUseCase.findById(PROFISSIONAL_ID);

        // Assert
        assertNotNull(result);
        assertSame(profissional1, result);
        verify(profissionalRepository, times(1)).findById(PROFISSIONAL_ID);
    }

    @Test
    @DisplayName("Deve lançar ProfissionalNotFoundException quando profissional não for encontrado")
    void shouldThrowProfissionalNotFoundExceptionWhenProfissionalNotFound() {
        // Arrange
        when(profissionalRepository.findById(PROFISSIONAL_ID)).thenReturn(Optional.empty());

        // Act & Assert
        ProfissionalNotFoundException exception = assertThrows(ProfissionalNotFoundException.class,
            () -> readProfissionalUseCase.findById(PROFISSIONAL_ID));

        verify(profissionalRepository, times(1)).findById(PROFISSIONAL_ID);
    }
}
