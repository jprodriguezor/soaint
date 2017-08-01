package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
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

    public GestionarCorrespondenciaWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "radicarCorrespondencia", operationName = "radicarCorrespondencia")
    public ComunicacionOficialDTO radicarCorrespondencia(@WebParam(name = "comunicacion_oficial") final ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    @WebMethod(action = "registrarObservacionCorrespondencia", operationName = "registrarObservacionCorrespondencia")
    public void registrarObservacionCorrespondencia(@WebParam(name = "traza_documento") final PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws BusinessException, SystemException {
        boundary.registrarObservacionCorrespondencia(ppdTrazDocumentoDTO);
    }

    @WebMethod(action = "listarCorrespondenciaByNroRadicado", operationName = "listarCorrespondenciaByNroRadicado")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    @WebMethod(action = "actualizarEstadoCorrespondencia", operationName = "actualizarEstadoCorrespondencia")
    public void actualizarEstadoCorrespondencia(@WebParam(name = "correspondencia") final CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    @WebMethod(action = "actualizarIdeInstancia", operationName = "actualizarIdeInstancia")
    public void actualizarIdeInstancia(@WebParam(name = "correspondencia") final CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        boundary.actualizarIdeInstancia(correspondenciaDTO);
    }

    @WebMethod(action = "listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", operationName = "listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado")
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(@WebParam(name = "fecha_ini") final Date fechaIni,
                                                                                                  @WebParam(name = "fecha_fin") final Date fechaFin,
                                                                                                  @WebParam(name = "cod_dependencia") final String codDependencia,
                                                                                                  @WebParam(name = "cod_estado") final String codEstado,
                                                                                                  @WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado);
    }
}
