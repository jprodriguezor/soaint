package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.OrganigramaAdministrativoControl;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 22-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarOrganigramaAdministrativo {
    // [fields] -----------------------------------

    @Autowired
    OrganigramaAdministrativoControl control;

    // ----------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarDescendientesDirectosDeElementoRayz() throws BusinessException, SystemException {
        return control.listarDescendientesDirectosDeElementoRayz();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama() throws BusinessException, SystemException {
        return control.consultarOrganigrama();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(BigInteger ideOrgaAdmin) throws SystemException {
        return control.listarElementosDeNivelInferior(ideOrgaAdmin);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) throws BusinessException, SystemException {
        return control.listarPadreDeSegundoNivel(ideOrgaAdmin);
    }

}
