package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.ConstantesControl;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarConstantes {

    // [fields] -----------------------------------

    @Autowired
    private ConstantesControl control;

    // ----------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByEstado(String estado) throws SystemException {
        return control.listarConstantesByEstado(estado);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodigoAndEstado(String codigo, String estado) throws SystemException {
        return control.listarConstantesByCodigoAndEstado(codigo, estado);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodPadreAndEstado(String codPadre, String estado)throws SystemException{
        return control.listarConstantesByCodPadreAndEstado(codPadre, estado);
    }
}
