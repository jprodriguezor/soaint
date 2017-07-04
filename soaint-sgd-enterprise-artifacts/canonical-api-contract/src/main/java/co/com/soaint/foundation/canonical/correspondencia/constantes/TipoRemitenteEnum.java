package co.com.soaint.foundation.canonical.correspondencia.constantes;

/**
 * Created by esanchez on 7/4/2017.
 */
public enum TipoRemitenteEnum {
    EXTERNO("EXT", "EXTERNO"),
    INTERNO("INT", "INTERNO");

    private final String codigo;
    private final String nombre;

    private TipoRemitenteEnum(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo(){ return codigo; }
    public String getNombre(){ return nombre; }
}
