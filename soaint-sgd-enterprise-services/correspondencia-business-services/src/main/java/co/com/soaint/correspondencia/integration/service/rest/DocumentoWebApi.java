package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/documento-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class DocumentoWebApi {

    private static Logger logger = LogManager.getLogger(AsignacionWebApi.class.getName());

    @Autowired
    GestionarDocumento boundary;

    public DocumentoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @PUT
    @Path("/documento/actualizar-referencia-ecm")
    public void actualizarReferenciaECM(DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        logger.info("processing rest request - actualizar referencia ECM");
        boundary.actualizarReferenciaECM(documentoDTO);
    }

    @POST
    @Path("/documento/registrar-observacion")
    public void registrarObservacion(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws BusinessException, SystemException{
        logger.info("processing rest request - registrar observacion a un documento");
        boundary.generarTrazaDocumento(ppdTrazDocumentoDTO);
    }

    @GET
    @Path("/documento/listar-observaciones/{ide-documento}")
    public ObservacionesDocumentoDTO listarObservacionesDocumento(@PathParam("ide-documento")final BigInteger ideDocumento) throws SystemException{
        logger.info("processing rest request - listar observaciones de un documento");
        return boundary.listarObservacionesDocumento(ideDocumento);
    }
}
