package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Log4j2
@Getter
@AllArgsConstructor
public enum TransferType implements Serializable {

    PRIMARY("Primaria"),
    SECONDARY("Secundaria");

    private static final Long serialVersionUID = 199L;

    private final String transfer;

    public static TransferType getTransferBy(@NotNull String transferOrName) {
        final String tmpName = transferOrName;
        final Stream<TransferType> stream = Arrays
                .stream(TransferType.values());
        final Optional<TransferType> optionalType = stream
                .filter
                        (type -> type.getTransfer().equalsIgnoreCase(tmpName)
                                || type.name().equalsIgnoreCase(tmpName))
                .findFirst();
        return optionalType.orElse(null);
    }
}
