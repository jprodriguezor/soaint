package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum PhaseType implements Serializable {

    ARCHIVO_CENTRAL("AC", "Archivo Central"),
    ARCHIVO_GESTION("AG", "Archivo Gestion");

    private static final Long serialVersionUID = 1L;

    private final String key;
    private final String name;
}
