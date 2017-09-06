package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionesOficialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;

/**
 * Created by esanchez on 6/15/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarCorrespondenciaWS {

    @Autowired
    GestionarCorrespondencia boundary;

    /**
     * Constructor
     */
    public GestionarCorrespondenciaWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "radicarCorrespondencia", operationName = "radicarCorrespondencia")
    public ComunicacionOficialDTO radicarCorrespondencia(@WebParam(name = "comunicacion_oficial") final ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarCorrespondenciaByNroRadicado", operationName = "listarCorrespondenciaByNroRadicado")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "actualizarEstadoCorrespondencia", operationName = "actualizarEstadoCorrespondencia")
    public void actualizarEstadoCorrespondencia(@WebParam(name = "correspondencia") final CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "actualizarIdeInstancia", operationName = "actualizarIdeInstancia")
    public void actualizarIdeInstancia(@WebParam(name = "correspondencia") final CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        boundary.actualizarIdeInstancia(correspondenciaDTO);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codEstado
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", operationName = "listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado")
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(@WebParam(name = "fecha_ini") final Date fechaIni,
                                                                                                                @WebParam(name = "fecha_fin") final Date fechaFin,
                                                                                                                @WebParam(name = "cod_dependencia") final String codDependencia,
                                                                                                                @WebParam(name = "cod_estado") final String codEstado,
                                                                                                                @WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado);
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
    @WebMethod(action = "listarCorrespondenciaByPeriodoAndCodDependenciaAndTipologiaDocumentalAndNroRadicado", operationName = "listarCorrespondenciaByPeriodoAndCodDependenciaAndTipologiaDocumentalAndNroRadicado")
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndTipologiaDocumentalAndNroRadicado(@WebParam(name = "fecha_ini") final Date fechaIni,
                                                                                                                          @WebParam(name = "fecha_fin") final Date fechaFin,
                                                                                                                          @WebParam(name = "cod_dependencia") final String codDependencia,
                                                                                                                          @WebParam(name = "cod_tipologia_documental") final String codTipoDoc,
                                                                                                                          @WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByPeriodoAndCodDependenciaAndTipologiaDocumentalAndNroRadicado(fechaIni, fechaFin, codDependencia, codTipoDoc, nroRadicado);
    }

    /**
     * 
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "verificarByNroRadicado", operationName = "verificarByNroRadicado")
    public Boolean verificarByNroRadicado(@WebParam(name = "nro_radicado")final String nroRadicado) throws SystemException{
        return boundary.verificarByNroRadicado(nroRadicado);
    }
}
