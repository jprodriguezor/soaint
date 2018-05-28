package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum FinalDispositionType implements Serializable {

    CONSERVACION_TOTAL("CT", "Conservacion Total"),
    ELIMINAR("E", "Eliminar"),
    SELECCIONAR("S", "Seleccionar"),
    MICROFILMAR("M", "Microfilmar"),
    DIGITALIZAR("D", "Digitalizar");

    private static final Long serialVersionUID = 1L;

    private final String key;
    private final String name;
}
