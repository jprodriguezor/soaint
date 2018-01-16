package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 9/6/2017.
 */
@Getter
@AllArgsConstructor
public enum TipoCausalEnum {
    MALACALIDADIMAGEN(1, "MALACALIDADIMAGEN"),
    DATOSINCORRECTOS(2, "DATOSINCORRECTOS"),
    NUMEROREINTENTOSMAXIMO(3, "NUMEROREINTENTOSMAXIMO");

    private final int codigo;
    private final String nombre;
}
