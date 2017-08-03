package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.CorrespondenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionesOficialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 31-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarCorrespondencia {

    // [fields] -----------------------------------

    @Autowired
    CorrespondenciaControl control;

    // ----------------------

    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return control.radicarCorrespondencia(comunicacionOficialDTO);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizarIdeInstancia(correspondenciaDTO);
    }

    public void registrarObservacionCorrespondencia(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws SystemException{
        control.registrarObservacionCorrespondencia(ppdTrazDocumentoDTO);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado);
    }
}
