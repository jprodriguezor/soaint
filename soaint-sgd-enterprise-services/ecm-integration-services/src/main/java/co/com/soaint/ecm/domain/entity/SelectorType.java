package co.com.soaint.ecm.domain.entity;

import co.com.soaint.ecm.util.ConstantesECM;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Log4j2
public enum SelectorType implements Serializable {

    PD("", ConstantesECM.PRODUCCION_DOCUMENTAL),
    EE(ConstantesECM.COMUNICACION_EXTERNA, ConstantesECM.COMUNICACIONES_EXTERNAS_RECIBIDAS),
    EI(ConstantesECM.COMUNICACION_INTERNA, ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS),
    SE(ConstantesECM.COMUNICACION_EXTERNA, ConstantesECM.COMUNICACIONES_EXTERNAS_ENVIADAS),
    SI(ConstantesECM.COMUNICACION_INTERNA, ConstantesECM.COMUNICACIONES_INTERNAS_ENVIADAS);

    private static final Long serialVersionUID = 121L;
    private final String fatherFolderName;
    private final String selectorName;

    SelectorType(String fatherFolderName, String selectorName) {
        LocalDate localDate = LocalDate.now();
        this.selectorName = selectorName + localDate.getYear();
        this.fatherFolderName = fatherFolderName;
    }

    public static SelectorType getSelectorBy(String nroRadicado) {
        log.info("Searching for the selector of communication");
        if (!StringUtils.isEmpty(nroRadicado) && nroRadicado.length() >= 2) {
            final SelectorType[] values = SelectorType.values();
            nroRadicado = nroRadicado.toUpperCase();
            for (SelectorType selectorType : values) {
                if (nroRadicado.contains(selectorType.name())) {
                    log.info("Retrieving selector {}", selectorType.name());
                    return selectorType;
                }
            }
        }
        log.info("None selector was found!!, retrieving null result");
        return null;
    }
}
