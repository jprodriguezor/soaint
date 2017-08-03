package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.MunicipioControl;
import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
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
@BusinessBoundary
public class GestionarMunicipio {

    // [fields] -----------------------------------

    @Autowired
    private MunicipioControl control;

    // ----------------------

    public GestionarMunicipio() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByCodDeparAndEstado(String codDepar, String estado) throws SystemException {
        return control.listarMunicipiosByCodDeparAndEstado(codDepar, estado);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByEstado(String estado) throws SystemException {
        return control.listarMunicipiosByEstado(estado);
    }
}
