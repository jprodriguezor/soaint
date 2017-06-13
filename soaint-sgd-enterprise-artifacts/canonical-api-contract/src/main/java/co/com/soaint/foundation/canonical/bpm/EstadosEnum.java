package co.com.soaint.foundation.canonical.bpm;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by Arce on 6/9/2017.
 */
@XmlType
public enum EstadosEnum {

    CREADO, LISTO, RESERVADO, ENPROGRESO, SUSPENDIDO, COMPLETADO, FALLIDO, ERROR, SALIDO, OBSOLETO

}
