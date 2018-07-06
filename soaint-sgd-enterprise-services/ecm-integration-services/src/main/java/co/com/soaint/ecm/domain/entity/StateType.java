package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Log4j2
@AllArgsConstructor
public enum StateType implements Serializable {

    REJECTED("Rechazado"),
    APPROVED("Aprobado");

    private static final Long serialVersionUID = 199L;

    private final String stateName;

    public static StateType getStateBy(@NotNull String stateOrName) {
        final String tmpState = stateOrName;
        final Stream<StateType> stream = Arrays
                .stream(StateType.values());
        final Optional<StateType> optionalType = stream
                .filter
                        (type -> type.getStateName().equalsIgnoreCase(tmpState)
                                || type.name().equalsIgnoreCase(tmpState))
                .findFirst();
        return optionalType.orElse(null);
    }
}
