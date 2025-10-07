package fiap.tech.challenge.hospital_manager.controller.auth;

import fiap.tech.challenge.hospital_manager.dto.in.AuthIn;
import fiap.tech.challenge.hospital_manager.presenter.auth.AuthPresenter;
import fiap.tech.challenge.hospital_manager.security.CustomUserDetails;
import fiap.tech.challenge.hospital_manager.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private AuthController authController;

    private AuthIn authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthIn("user", "pass");
    }

    @Test
    void deveRetornarTokenQuandoLoginValido() {
        // Arrange
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user");
        when(userDetails.getAuthorities()).thenReturn(List.of(new SimpleGrantedAuthority("ROLE_MEDICO")));
        when(jwtUtil.generateToken(eq("user"), anyMap())).thenReturn("fake-jwt-token");

        // Act
        ResponseEntity<?> response = authController.login(authRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(AuthPresenter.toResponse("fake-jwt-token"), response.getBody());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(eq("user"), anyMap());
    }

    @Test
    void deveRetornar401QuandoCredenciaisInvalidas() {
        // Arrange
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act
        ResponseEntity<?> response = authController.login(authRequest);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertEquals("Credenciais inválidas", response.getBody());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(any(), anyMap());
    }
}
