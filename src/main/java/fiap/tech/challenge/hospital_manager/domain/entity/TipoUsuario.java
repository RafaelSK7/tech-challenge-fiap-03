package fiap.tech.challenge.hospital_manager.domain.entity;

import lombok.Getter;

@Getter
public enum TipoUsuario {

    ENFERMEIRO("enfermeiro"), MEDICO("medico"), PACIENTE("paciente");

    private String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public static TipoUsuario fromDescricao(String descricao) {
        for (TipoUsuario e : values()) {
            if (e.getDescricao().equalsIgnoreCase(descricao)) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }
}
