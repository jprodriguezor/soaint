package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AsignacionControl;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncAsigDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarAsignacion {
    // [fields] -----------------------------------

    @Autowired
    AsignacionControl control;

    // ----------------------

    /**
     *
     * @param asignacionesDTO
     * @return
     * @throws SystemException
     */
    public AsignacionesDTO asignarCorrespondencia(AsignacionesDTO asignacionesDTO) throws BusinessException, SystemException {
        return control.asignarCorrespondencia(asignacionesDTO);
    }

    /**
     *
     * @param asignacion
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdInstancia(AsignacionDTO asignacion) throws BusinessException, SystemException {
        control.actualizarIdInstancia(asignacion);
    }

    /**
     *
     * @param asignacion
     * @throws SystemException
     */
    public void actualizarTipoProceso(AsignacionDTO asignacion) throws SystemException {
        control.actualizarTipoProceso(asignacion);
    }

    /**
     *
     * @param ideFunci
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(BigInteger ideFunci, String nroRadicado) throws BusinessException, SystemException {
       return control.listarAsignacionesByFuncionarioAndNroRadicado(ideFunci, nroRadicado);
    }

    /**
     *
     * @param ideAgente
     * @return
     * @throws SystemException
     */
    public FuncAsigDTO consultarAsignacionReasignarByIdeAgente(BigInteger ideAgente) throws SystemException {
        return control.consultarAsignacionReasignarByIdeAgente(ideAgente);
    }

    /**
     *
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    public void asignarDocumentoByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        control.asignarDocumentoByNroRadicado(nroRadicado);
    }
}
