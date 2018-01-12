package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 8/8/2017.
 */
@Getter
@AllArgsConstructor
public enum CodigoTipoUbicacion {
    ECM("CM", "ECM"),
    FISICA("FS", "FISICA");

    private final String codigo;
    private final String nombre;
}
