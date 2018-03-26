package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

public interface RecordControl {

    /**
     * Permite cerrar una unidad documental
     *
     * @param unidadDocumentalDTO Objeto Unidad documental a cerrar
     * @return boolean Indica si se ha cerrado con exito
     * @throws SystemException SystemException
     */
    boolean cerrarUnidadDocumentalRecord(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;
}
