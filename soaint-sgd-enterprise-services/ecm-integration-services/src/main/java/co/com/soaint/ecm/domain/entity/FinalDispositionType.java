package co.com.soaint.ecm.domain.entity;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum FinalDispositionType implements Serializable {

    CT("1", "Conservacion Total"),
    E("2", "Eliminar"),
    S("3", "Seleccionar"),
    M("4", "Microfilmar"),
    D("5", "Digitalizar"),
    TP("", "Transferencia Primaria"),
    TS("", "Transferencia Secundaria");

    private static final Long serialVersionUID = 1L;

    private final String dispositionKey;
    private final String dispositionName;

    public static FinalDispositionType getDispositionBy(String nameOrKey) {
        if (StringUtils.isEmpty(nameOrKey)) {
            return null;
        }
        nameOrKey = Utilities.reemplazarCaracteresRaros(nameOrKey).trim();
        final String tmpDisposition = nameOrKey;
        final Stream<FinalDispositionType> stream = Arrays.stream(FinalDispositionType.values());
        final Optional<FinalDispositionType> optionalType = stream
                .filter(type -> type.getDispositionName().equalsIgnoreCase(tmpDisposition)
                        || type.getDispositionKey().equals(tmpDisposition)
                        || type.name().equalsIgnoreCase(tmpDisposition))
                .findFirst();
        return optionalType.orElse(null);
    }
}
