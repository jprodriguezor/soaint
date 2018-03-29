package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.CorrespondenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionesOficialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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

    /**
     *
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return control.radicarCorrespondencia(comunicacionOficialDTO);
    }

    /**
     *
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     *
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialFullDTO listarFullCorrespondenciaByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        return control.listarFullCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     *
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    /**
     *
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        control.actualizarIdeInstancia(correspondenciaDTO);
    }

    /**
     *
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codEstado
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(Date fechaIni, Date fechaFin, String codDependencia, String codEstado, String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado);
    }

    /**
     * 
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codTipoDoc
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionesOficialesDTO listarCorrespondenciaSinDistribuir(Date fechaIni, Date fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) throws BusinessException, SystemException {
        return control.listarCorrespondenciaSinDistribuir(fechaIni, fechaFin, codDependencia, codTipoDoc, nroRadicado);
    }

    /**
     *
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public Boolean verificarByNroRadicado(String nroRadicado) throws SystemException{
        return control.verificarByNroRadicado(nroRadicado);
    }

    /**
     *
     * @param comunicacionOficialDTO
     * @return
     * @throws SystemException
     */
    public String actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException{
        return control.actualizarComunicacion(comunicacionOficialDTO);
    }

    /**
     *
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ComunicacionOficialDTO radicarCorrespondenciaSalida(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return control.radicarCorrespondenciaSalida(comunicacionOficialDTO);
    }
}
