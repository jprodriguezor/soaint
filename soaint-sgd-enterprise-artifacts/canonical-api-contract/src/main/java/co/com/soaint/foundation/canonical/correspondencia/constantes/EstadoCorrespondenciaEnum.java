package co.com.soaint.foundation.canonical.correspondencia.constantes;

/**
 * Created by esanchez on 7/3/2017.
 */
public enum EstadoCorrespondenciaEnum {
    RADICADO("RD", "RADICADO"),
    SIN_ASIGNAR("SA", "SIN ASIGNAR"),
    ASIGNADO("AS", "ASIGNADO");

    private final String codigo;
    private final String nombre;

    private EstadoCorrespondenciaEnum(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo(){ return codigo; }
    public String getEstado(){ return nombre; }
}
