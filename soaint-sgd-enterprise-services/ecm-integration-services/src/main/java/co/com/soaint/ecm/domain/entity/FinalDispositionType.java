package co.com.soaint.ecm.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum FinalDispositionType implements Serializable {

    CONSERVACION_TOTAL,
    ELIMINAR,
    SELECCIONAR,
    MICROFILMAR,
    DIGITALIZAR;

    private static final Long serialVersionUID = 1L;

    public boolean containsDisposition(String disposition) {
        switch (this) {
            case SELECCIONAR:
                return getAllSeleccionar().contains(disposition);
            case MICROFILMAR:
                return getAllMicrofilmar().contains(disposition);
            case DIGITALIZAR:
                return getAllDigitalizar().contains(disposition);
            case ELIMINAR:
                return getAllEliminar().contains(disposition);
            case CONSERVACION_TOTAL:
                return getAllTotalConservation().contains(disposition);
                default:
                    return false;
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

    private List<String> getAllTotalConservation() {
        final List<String> resultList = new ArrayList<>();
        resultList.add(resultList.size(), "Conservacion Total");
        resultList.add(resultList.size(), "Conservación Total");
        resultList.add(resultList.size(), "conservación total");
        resultList.add(resultList.size(), "conservacion total");
        resultList.add(resultList.size(), "CT");
        resultList.add(resultList.size(), "ct");
        return resultList;
    }

    private List<String> getAllEliminar() {
        final List<String> resultList = new ArrayList<>();
        resultList.add(resultList.size(), "Eliminar");
        resultList.add(resultList.size(), "eliminar");
        resultList.add(resultList.size(), "e");
        resultList.add(resultList.size(), "E");
        return resultList;
    }

    private List<String> getAllSeleccionar() {
        final List<String> resultList = new ArrayList<>();
        resultList.add(resultList.size(), "Seleccionar");
        resultList.add(resultList.size(), "seleccionar");
        resultList.add(resultList.size(), "S");
        resultList.add(resultList.size(), "s");
        return resultList;
    }

    private List<String> getAllMicrofilmar() {
        final List<String> resultList = new ArrayList<>();
        resultList.add(resultList.size(), "Microfilmar");
        resultList.add(resultList.size(), "microfilmar");
        resultList.add(resultList.size(), "M");
        resultList.add(resultList.size(), "m");
        return resultList;
    }

    private List<String> getAllDigitalizar() {
        final List<String> resultList = new ArrayList<>();
        resultList.add(resultList.size(), "Digitalizar");
        resultList.add(resultList.size(), "digitalizar");
        resultList.add(resultList.size(), "D");
        resultList.add(resultList.size(), "d");
        return resultList;
    }
}
