package co.com.soaint.correspondencia.domain.entity.constantes;

/**
 * Created by esanchez on 6/16/2017.
 */
public enum TipoAgenteEnum {
    EXTERNO("EXT", "EXTERNO"),
    INTERNO("INT", "INTERNO");

    private final String codigo;
    private final String nombre;

    private TipoAgenteEnum(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo(){ return codigo; }
    public String getNombre(){ return nombre; }
}
