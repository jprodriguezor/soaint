package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PaisControl;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
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
 * Created: 23-May-2017
 * Author: jprodriguezor
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarPais {

    // [fields] -----------------------------------

    @Autowired
    private PaisControl control;
    // ----------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByEstado(String estado) throws SystemException{
        return control.listarPaisesByEstado(estado);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByNombrePaisAndEstado(String nombrePais, String estado) throws SystemException{
        return control.listarPaisesByNombrePaisAndEstado(nombrePais, estado);
    }
}
