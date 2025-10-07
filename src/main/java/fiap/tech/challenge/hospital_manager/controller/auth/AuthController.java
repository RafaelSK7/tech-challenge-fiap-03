package fiap.tech.challenge.hospital_manager.controller.auth;

import fiap.tech.challenge.hospital_manager.dto.in.AuthIn;
import fiap.tech.challenge.hospital_manager.presenter.auth.AuthPresenter;
import fiap.tech.challenge.hospital_manager.security.CustomUserDetails;
import fiap.tech.challenge.hospital_manager.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager am, JwtUtil ju) {
        this.authManager = am;
        this.jwtUtil = ju;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthIn req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

            String token = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    Map.of("role", userDetails.getAuthorities())
            );

            return ResponseEntity.ok(AuthPresenter.toResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
