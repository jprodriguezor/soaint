package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by esanchez on 7/3/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoCorrespondenciaEnum {
    RADICADO("RD", "RADICADO"),
    REGISTRADO("RG", "REGISTRADO"),
    SIN_ASIGNAR("SA", "SIN ASIGNAR"),
    ASIGNACION("AS", "ASIGNACION");

    private final String codigo;
    private final String nombre;
}
