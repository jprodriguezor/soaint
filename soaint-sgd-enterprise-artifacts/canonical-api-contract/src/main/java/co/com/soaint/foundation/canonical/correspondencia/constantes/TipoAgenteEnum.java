package co.com.soaint.foundation.canonical.correspondencia.constantes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by esanchez on 7/4/2017.
 */
@Getter
@AllArgsConstructor
public enum TipoAgenteEnum {
    REMITENTE("TP-AGEE", "REMITENTE"),
    DESTINATARIO("TP-AGEI", "DESTINATARIO");

    private final String codigo;
    private final String nombre;
}
