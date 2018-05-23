package co.com.soaint.ecm.domain.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum PhaseType implements Serializable {

    ARCHIVO_CENTRAL("AC", "Archivo Central", ""),
    ARCHIVO_GESTION("AG", "Archivo Gestion", "Archivo Gestión");

    private static final Long serialVersionUID = 1L;

    private final List<String> fileList;

    PhaseType(String key, String s0, String s1) {
        fileList = new ArrayList<>();
        fileList.add(fileList.size(), key);
        fileList.add(fileList.size(), key.toLowerCase());
        fileList.add(fileList.size(), s0);
        fileList.add(fileList.size(), s0.toLowerCase());
        fileList.add(fileList.size(), s0.toUpperCase());
        if (!StringUtils.isEmpty(s1)) {
            fileList.add(fileList.size(), s1);
            fileList.add(fileList.size(), s1.toLowerCase());
            fileList.add(fileList.size(), s1.toUpperCase());
        }
    }

    public String getValueAt(int position) {
        if (position >= 0 && position < fileList.size()) {
            return fileList.get(position);
        }
        return "";
    }

    public boolean containPhase(String phase) {
        return fileList.contains(phase);
    }

    public String getInPhases() {
        final int length = fileList.size();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final String disposition = fileList.get(i);
            final String comma = (i == length - 1) ? "" : ",";
            builder.append("'").append(disposition).append("'").append(comma);
        }
        return builder.toString();
    }
}
