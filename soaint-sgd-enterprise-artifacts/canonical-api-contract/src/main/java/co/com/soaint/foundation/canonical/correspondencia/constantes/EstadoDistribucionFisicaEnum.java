package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 10/26/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadoDistribucionFisicaEnum {

    SIN_DISTRIBUIR("SD", "SIN DISTRIBUIR"),
    EMPLANILLADO("EM", "EMPLANILLADO");

    private final String codigo;
    private final String nombre;
}
