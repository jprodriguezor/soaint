package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum PhaseType implements Serializable {
    ARCHIVO_CENTRAL("archivo central"),
    ARCHIVO_GESTION("archivo gestion");

    private static final Long serialVersionUID = 1L;
    private final String texto;
}
