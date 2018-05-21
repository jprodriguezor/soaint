package co.com.soaint.ecm.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum PhaseType implements Serializable {

    ARCHIVO_CENTRAL("AC", "Archivo Central", ""),
    ARCHIVO_GESTION("AG", "Archivo Gestion", "Archivo Gestión");

    private static final Long serialVersionUID = 1L;

    private final List<String> allCentralFile = new ArrayList<>();
    private final List<String> allManagementFile = new ArrayList<>();

    PhaseType(String key, String s0, String s1) {
        switch (key) {
            case "AC":
                allCentralFile.add(allCentralFile.size(), key);
                allCentralFile.add(allCentralFile.size(), key.toLowerCase());
                allCentralFile.add(allCentralFile.size(), s0);
                allCentralFile.add(allCentralFile.size(), s0.toLowerCase());
                allCentralFile.add(allCentralFile.size(), s0.toUpperCase());
                break;
            case "AG":
                allManagementFile.add(allManagementFile.size(), key);
                allManagementFile.add(allManagementFile.size(), key.toLowerCase());
                allManagementFile.add(allManagementFile.size(), s0);
                allManagementFile.add(allManagementFile.size(), s0.toLowerCase());
                allManagementFile.add(allManagementFile.size(), s0.toUpperCase());
                allManagementFile.add(allManagementFile.size(), s1);
                allManagementFile.add(allManagementFile.size(), s1.toLowerCase());
                allManagementFile.add(allManagementFile.size(), s1.toUpperCase());
                break;
        }
    }

    public String getValueAt(int position) {
        switch (this) {
            case ARCHIVO_CENTRAL:
                final int centralFileSize = allCentralFile.size();
                if (position >= 0 && position < centralFileSize) {
                    return allCentralFile.get(position);
                }
                break;
            default:
                final int managementFileSize = allManagementFile.size();
                if (position >= 0 && position < managementFileSize) {
                    return allManagementFile.get(position);
                }
                break;
        }
        return "";
    }

    public boolean containPhase(String phase) {
        switch (this) {
            case ARCHIVO_CENTRAL:
                return allCentralFile.contains(phase);
            default:
                return allManagementFile.contains(phase);
        }
    }

    public String getInPhases() {
        return this == ARCHIVO_CENTRAL ? getInPhases(allCentralFile)
                : getInPhases(allManagementFile);
    }

    private String getInPhases(List<String> list) {
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
