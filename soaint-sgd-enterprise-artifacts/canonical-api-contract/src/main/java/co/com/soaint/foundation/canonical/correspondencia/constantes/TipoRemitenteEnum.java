package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 7/4/2017.
 */
@Getter
@AllArgsConstructor
public enum TipoRemitenteEnum {
    EXTERNO("EXT", "EXTERNO"),
    INTERNO("INT", "INTERNO");

    private final String codigo;
    private final String nombre;
}
