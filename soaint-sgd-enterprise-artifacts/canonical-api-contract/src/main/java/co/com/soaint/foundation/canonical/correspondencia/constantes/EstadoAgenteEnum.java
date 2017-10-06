package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 8/8/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoAgenteEnum {
    SIN_ASIGNAR("SA", "SIN ASIGNAR"),
    ASIGNADO("AS", "ASIGNADO"),
    DISTRIBUCION("DT", "DISTRIBUCION"),
    EMPLANILLADO("EM", "EMPLANILLADO"),
    TRAMITADO("TD", "TRAMITADO");

    private final String codigo;
    private final String nombre;
}
