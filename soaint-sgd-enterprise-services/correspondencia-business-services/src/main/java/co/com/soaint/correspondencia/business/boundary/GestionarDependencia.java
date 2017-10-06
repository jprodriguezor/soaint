package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DependenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciasDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 23-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarDependencia {
    // [fields] -----------------------------------

    @Autowired
    DependenciaControl control;

    // ----------------------

    /**
     *
     * @param codOrg
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public DependenciaDTO listarDependenciaByCodigo(String codOrg)throws BusinessException, SystemException {
        return control.listarDependenciaByCodigo(codOrg);
    }

    /**
     *
     * @param codigos
     * @return
     * @throws SystemException
     */
    public DependenciasDTO listarDependenciasByCodigo(String[] codigos)throws SystemException{
        return control.listarDependenciasByCodigo(codigos);
    }

    /**
     *
     * @return
     * @throws SystemException
     */
    public DependenciasDTO listarDependencias() throws SystemException {
        return control.listarDependencias();
    }
}
