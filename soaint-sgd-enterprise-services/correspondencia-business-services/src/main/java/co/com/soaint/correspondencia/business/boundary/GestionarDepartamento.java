package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DepartamentoControl;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class GestionarDepartamento {

    // [fields] -----------------------------------

    @Autowired
    private DepartamentoControl control;

    // ----------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByEstado(String estado) throws SystemException {
       return control.listarDepartamentosByEstado(estado);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByCodPaisAndEstado(String codPais, String estado) throws SystemException {
        return control.listarDepartamentosByCodPaisAndEstado(codPais, estado);
    }
}
