package fiap.tech.challenge.hospital_manager.security;

import fiap.tech.challenge.hospital_manager.domain.entity.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean enabled;

    public CustomUserDetails(Long id, String username, String password, List<GrantedAuthority> authorities, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public static CustomUserDetails fromUsuario(Usuario usuario) {
        // ROLE_ prefix é obrigatório no Spring Security
        var role = "ROLE_" + usuario.getTipoUsuario().name();
        return new CustomUserDetails(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority(role)),
                true
        );
    }
}
