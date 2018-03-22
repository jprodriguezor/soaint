package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AnexoControl;
import co.com.soaint.foundation.canonical.correspondencia.AnexosFullDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 25-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarAnexo {

    // [fields] -----------------------------------

    @Autowired
    private AnexoControl control;

    // ----------------------

    /**
     *
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AnexosFullDTO listarAnexosByNroRadicado(String nroRadicado) throws SystemException {
       return control.listarAnexosByNroRadicado(nroRadicado);
    }
}
