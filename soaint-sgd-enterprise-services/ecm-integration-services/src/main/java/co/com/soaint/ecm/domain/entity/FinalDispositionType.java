package co.com.soaint.ecm.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter(value = AccessLevel.PRIVATE)
public enum FinalDispositionType implements Serializable {

    CONSERVACION_TOTAL("CT", "Conservacion Total", "Conservación Total"),
    ELIMINAR("E", "Eliminar", ""),
    SELECCIONAR("S", "Seleccionar", ""),
    MICROFILMAR("M", "Microfilmar", ""),
    DIGITALIZAR("D", "Digitalizar", "");

    private static final Long serialVersionUID = 1L;

    private final List<String> allTotalConservation = new ArrayList<>();
    private final List<String> allEliminar = new ArrayList<>();
    private final List<String> allSeleccionar = new ArrayList<>();
    private final List<String> allMicrofilmar = new ArrayList<>();
    private final List<String> allDigitalizar = new ArrayList<>();

    FinalDispositionType(String key, String s0, String s1) {
        switch (key) {
            case "CT":
                allTotalConservation.add(allTotalConservation.size(), key);
                allTotalConservation.add(allTotalConservation.size(), key.toLowerCase());
                allTotalConservation.add(allTotalConservation.size(), s0);
                allTotalConservation.add(allTotalConservation.size(), s0.toUpperCase());
                allTotalConservation.add(allTotalConservation.size(), s0.toLowerCase());
                allTotalConservation.add(allTotalConservation.size(), s1);
                allTotalConservation.add(allTotalConservation.size(), s1.toUpperCase());
                allTotalConservation.add(allTotalConservation.size(), s1.toLowerCase());
                break;
            case "E":
                allEliminar.add(allEliminar.size(), key);
                allEliminar.add(allEliminar.size(), key.toLowerCase());
                allEliminar.add(allEliminar.size(), s0);
                allEliminar.add(allEliminar.size(), s0.toUpperCase());
                allEliminar.add(allEliminar.size(), s0.toLowerCase());
                break;
            case "S":
                allSeleccionar.add(allSeleccionar.size(), key);
                allSeleccionar.add(allSeleccionar.size(), key.toLowerCase());
                allSeleccionar.add(allSeleccionar.size(), s0);
                allSeleccionar.add(allSeleccionar.size(), s0.toUpperCase());
                allSeleccionar.add(allSeleccionar.size(), s0.toLowerCase());
                break;
            case "M":
                allMicrofilmar.add(allMicrofilmar.size(), key);
                allMicrofilmar.add(allMicrofilmar.size(), key.toLowerCase());
                allMicrofilmar.add(allMicrofilmar.size(), s0);
                allMicrofilmar.add(allMicrofilmar.size(), s0.toUpperCase());
                allMicrofilmar.add(allMicrofilmar.size(), s0.toLowerCase());
                break;
            case "D":
                allDigitalizar.add(allDigitalizar.size(), key);
                allDigitalizar.add(allDigitalizar.size(), key.toLowerCase());
                allDigitalizar.add(allDigitalizar.size(), s0);
                allDigitalizar.add(allDigitalizar.size(), s0.toUpperCase());
                allDigitalizar.add(allDigitalizar.size(), s0.toLowerCase());
                break;
        }
    }

    public String getKey() {
        switch (this) {
            case SELECCIONAR:
                return allSeleccionar.get(0);
            case MICROFILMAR:
                return allMicrofilmar.get(0);
            case DIGITALIZAR:
                return allDigitalizar.get(0);
            case ELIMINAR:
                return allEliminar.get(0);
            default:
                return allTotalConservation.get(0);
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
            default:
                return allTotalConservation.contains(disposition);
        }
    }

    public String getInDispositions() {
        switch (this) {
            case SELECCIONAR:
                return getInDispositions(getAllSeleccionar());
            case MICROFILMAR:
                return getInDispositions(getAllMicrofilmar());
            case DIGITALIZAR:
                return getInDispositions(getAllDigitalizar());
            case ELIMINAR:
                return getInDispositions(getAllEliminar());
            case CONSERVACION_TOTAL:
                return getInDispositions(getAllTotalConservation());
            default:
                return "''";
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
