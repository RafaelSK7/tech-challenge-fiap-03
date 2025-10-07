package fiap.tech.challenge.hospital_manager.domain.entity;


import lombok.Getter;

@Getter
public enum Especialidade {
    CLINICA_GERAL("clinica_geral"),
    PEDIATRIA("pediatria"),
    CIRURGIA_GERAL("cirurgia_geral"),
    GINECOLOGIA_OBSTETRICIA("ginecologia"),
    ANESTESIOLOGIA("anestesiologia"),
    ORTOPEDIA_TRAUMATOLOGIA("ortopedia"),
    CARDIOLOGIA("cardiologia"),
    PSIQUIATRIA("psiquiatria"),
    DERMATOLOGIA("dermatologia"), NEUROLOGIA("neurologia"), ENDOCRINOLOGIA("endocrinologia"), GASTROENTEROLOGIA("gastroenterologia"), UROLOGIA("urologia"), RADIOLOGIA("radiologia"), OTORRINOLARINGOLOGIA("otorrinolaringologia"), INFECTOLOGIA("infectologia"), NEFROLOGIA("nefrologia"), ONCOLOGIA("oncologia"), REUMATOLOGIA("reumatologia"), GERIATRIA("geriatria"), ENFERMAGEM("enfermagem");

    private String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public static Especialidade fromDescricao(String descricao) {
        for (Especialidade e : values()) {
            if (e.getDescricao().equalsIgnoreCase(descricao)) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }
}
