package co.com.soaint.foundation.canonical.correspondencia.constantes;

/**
 * Created by esanchez on 7/4/2017.
 */
public enum TipoAgenteEnum {
    REMITENTE("REM", "REMITENTE"),
    DESTINATARIO("DES", "DESTINATARIO");

    private final String codigo;
    private final String nombre;

    private TipoAgenteEnum(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo(){ return codigo; }
    public String getNombre(){ return nombre; }
}
