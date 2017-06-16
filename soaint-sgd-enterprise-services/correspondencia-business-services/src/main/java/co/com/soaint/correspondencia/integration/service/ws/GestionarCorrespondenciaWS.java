package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.PathParam;

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
    public ComunicacionOficialDTO radicarCorrespondencia(@WebParam(name = "comunicacionOficial") final ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    @WebMethod(action = "listarCorrespondenciaByNroRadicado", operationName = "listarCorrespondenciaByNroRadicado")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@WebParam(name = "nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    @WebMethod(action = "actualizarEstadoCorrespondencia", operationName = "actualizarEstadoCorrespondencia")
    public void actualizarEstadoCorrespondencia(@WebParam(name = "correspondencia") final CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }
}
