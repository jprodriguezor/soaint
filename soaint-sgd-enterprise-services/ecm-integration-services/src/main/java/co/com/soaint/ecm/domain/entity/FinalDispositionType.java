package co.com.soaint.ecm.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.util.StringUtils;

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

    private final List<String> dispositionList;

    FinalDispositionType(String key, String s0, String s1) {
        dispositionList = new ArrayList<>();
        dispositionList.add(dispositionList.size(), key);
        dispositionList.add(dispositionList.size(), key.toLowerCase());
        dispositionList.add(dispositionList.size(), s0);
        dispositionList.add(dispositionList.size(), s0.toUpperCase());
        dispositionList.add(dispositionList.size(), s0.toLowerCase());
        if (!StringUtils.isEmpty(s1)) {
            dispositionList.add(dispositionList.size(), s1);
            dispositionList.add(dispositionList.size(), s1.toUpperCase());
            dispositionList.add(dispositionList.size(), s1.toLowerCase());
        }
    }

    public String getKey() {
        return dispositionList.get(0);
    }

    public boolean containsDisposition(String disposition) {
        return dispositionList.contains(disposition);
    }

    public String getInDispositions() {
        final int length = dispositionList.size();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final String disposition = dispositionList.get(i);
            final String comma = (i == length - 1) ? "" : ",";
            builder.append("'").append(disposition).append("'").append(comma);
        }
        return builder.toString();
    }
}
