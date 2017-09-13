package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 9/7/2017.
 */
@Getter
@AllArgsConstructor
public enum FormatoDocEnum {
    PDF("PDF", "PDF"),
    EXCEL("XLS", "EXCEL");

    private final String codigo;
    private final String nombre;
}
