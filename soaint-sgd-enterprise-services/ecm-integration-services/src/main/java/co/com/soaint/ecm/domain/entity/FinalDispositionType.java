package co.com.soaint.ecm.domain.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public enum FinalDispositionType implements Serializable {

    CONSERVACION_TOTAL("Conservacion Total", "Conservación Total", "ct"),
    ELIMINAR("Eliminar", "e", ""),
    SELECCIONAR("Seleccionar", "s", ""),
    MICROFILMAR("Microfilmar", "m", ""),
    DIGITALIZAR("Digitalizar", "d", "");

    private static final Long serialVersionUID = 1L;

    private final List<String> allTotalConservation = new ArrayList<>();
    private final List<String> allEliminar = new ArrayList<>();
    private final List<String> allSeleccionar = new ArrayList<>();
    private final List<String> allMicrofilmar = new ArrayList<>();
    private final List<String> allDigitalizar = new ArrayList<>();

    FinalDispositionType(String s, String s1, String s3) {

        allTotalConservation.add(allTotalConservation.size(), s);
        allTotalConservation.add(allTotalConservation.size(), s.toUpperCase());
        allTotalConservation.add(allTotalConservation.size(), s1);
        allTotalConservation.add(allTotalConservation.size(), s1.toUpperCase());

        allEliminar.add(allEliminar.size(), s);
        allEliminar.add(allEliminar.size(), s.toUpperCase());
        allEliminar.add(allEliminar.size(), s1);
        allEliminar.add(allEliminar.size(), s1.toUpperCase());

        allSeleccionar.add(allSeleccionar.size(), s);
        allSeleccionar.add(allSeleccionar.size(), s.toUpperCase());
        allSeleccionar.add(allSeleccionar.size(), s1);
        allSeleccionar.add(allSeleccionar.size(), s1.toUpperCase());

        allMicrofilmar.add(allMicrofilmar.size(), s);
        allMicrofilmar.add(allMicrofilmar.size(), s.toUpperCase());
        allMicrofilmar.add(allMicrofilmar.size(), s1);
        allMicrofilmar.add(allMicrofilmar.size(), s1.toUpperCase());

        allDigitalizar.add(allDigitalizar.size(), s);
        allDigitalizar.add(allDigitalizar.size(), s.toUpperCase());
        allDigitalizar.add(allDigitalizar.size(), s1);
        allDigitalizar.add(allDigitalizar.size(), s1.toUpperCase());

        if (!StringUtils.isEmpty(s3)) {
            allTotalConservation.add(allTotalConservation.size(), s3);
            allTotalConservation.add(allTotalConservation.size(), s3.toUpperCase());
        }
    }

    public boolean containsDisposition(String disposition) {
        switch (this) {
            case SELECCIONAR:
                return allSeleccionar.contains(disposition);
            case MICROFILMAR:
                return allMicrofilmar.contains(disposition);
            case DIGITALIZAR:
                return allDigitalizar.contains(disposition);
            case ELIMINAR:
                return allEliminar.contains(disposition);
            case CONSERVACION_TOTAL:
                return allTotalConservation.contains(disposition);
            default:
                return false;
        }
    }

    public String getInDispositions() {
        switch (this) {
            case SELECCIONAR:
                return getInDispositions(allSeleccionar);
            case MICROFILMAR:
                return getInDispositions(allMicrofilmar);
            case DIGITALIZAR:
                return getInDispositions(allDigitalizar);
            case ELIMINAR:
                return getInDispositions(allEliminar);
            case CONSERVACION_TOTAL:
                return getInDispositions(allTotalConservation);
            default:
                return "";
        }
    }

    private String getInDispositions(List<String> list) {
        final int length = list.size();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final String disposition = list.get(i);
            final String comma = (i == length - 1) ? "" : ",";
            builder.append("'").append(disposition).append("'").append(comma);
        }
        return builder.toString();
    }
}
