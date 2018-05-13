package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum FinalDispositionType implements Serializable {
    CONSERVACION_TOTAL("conservacion total"),
    ELIMINAR("eliminar"),
    SELECCIONAR("seleccionar"),
    MICROFILMAR("microfilmar"),
    DIGITALIZAR("digitalizar");

    private static final Long serialVersionUID = 1L;
    private final String texto;
}
