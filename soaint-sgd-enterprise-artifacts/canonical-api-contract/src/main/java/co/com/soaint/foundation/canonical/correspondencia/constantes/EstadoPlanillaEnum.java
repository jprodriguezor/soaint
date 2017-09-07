package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 9/6/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoPlanillaEnum {
    DISTRIBUCION("DT", "DISTRIBUCION"),
    ENTREGADO("EN", "ENTREGADO"),
    DEVUELTO("DV", "DEVUELTO"),
    ANULADO("AN", "ANULADO"),
    PENDIENTE("PD", "PENDIENTE");

    private final String codigo;
    private final String nombre;
}
