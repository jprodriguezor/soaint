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
public enum FinalDispositionType implements Serializable {

    CT("Conservacion Total"),
    E("Eliminar"),
    S("Seleccionar"),
    M("Microfilmar"),
    D("Digitalizar");

    private static final Long serialVersionUID = 1L;

    private final String dispositionName;

    public static FinalDispositionType getDispositionBy(String disposition) {
        disposition = Utilities.reemplazarCaracteresRaros(disposition).trim().toLowerCase();
        final String tmpDisposition = disposition;
        final Stream<FinalDispositionType> stream = Arrays
                .stream(FinalDispositionType.values());
        final Optional<FinalDispositionType> optionalType = stream
                .filter
                        (type -> type.getDispositionName().toLowerCase().equals(tmpDisposition))
                .findFirst();
        return optionalType.orElse(null);
    }
}
