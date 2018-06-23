package co.com.soaint.ecm.domain.entity;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PhaseType implements Serializable {

    AC("Archivo Central"),
    AG("Archivo Gestion");

    private static final Long serialVersionUID = 1L;

    private final String phaseName;

    public static PhaseType getPhaseTypeBy(String faseArchivo) {
        faseArchivo = Utilities.reemplazarCaracteresRaros(faseArchivo).trim().toLowerCase();
        final String tmpFaseArchivo = faseArchivo;
        final Stream<PhaseType> stream = Arrays
                .stream(PhaseType.values());
        final Optional<PhaseType> optionalType = stream
                .filter
                        (type -> type.getPhaseName().toLowerCase().equals(tmpFaseArchivo))
                .findFirst();
        return optionalType.orElse(null);
    }
}
