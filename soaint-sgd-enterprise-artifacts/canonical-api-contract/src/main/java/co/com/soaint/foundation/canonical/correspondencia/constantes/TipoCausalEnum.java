package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 9/6/2017.
 */
@Getter
@AllArgsConstructor
public enum TipoCausalEnum {
    MALACALIDADIMAGEN(1, "Calidad Imagen"),
    DATOSINCORRECTOS(2, "Datos incorrectos"),
    NUMEROREINTENTOSMAXIMO(3, "Supera los intentos permitidos de Redireccionamiento");

    private final int codigo;
    private final String nombre;
}
