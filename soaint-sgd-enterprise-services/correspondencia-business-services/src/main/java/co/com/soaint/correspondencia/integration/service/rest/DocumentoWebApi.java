package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class DocumentoWebApi {

    @Autowired
    GestionarDocumento boundary;

    /**
     * Constructor
     */
    public DocumentoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param documentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/documento/actualizar-referencia-ecm")
    public void actualizarReferenciaECM(DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar referencia ECM");
        boundary.actualizarReferenciaECM(documentoDTO);
    }

    /**
     * @param ppdTrazDocumentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/documento/registrar-observacion")
    public void registrarObservacion(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws BusinessException, SystemException {
        log.info("processing rest request - registrar observacion a un documento");
        boundary.generarTrazaDocumento(ppdTrazDocumentoDTO);
    }

    /**
     *
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/documento/listar-observaciones/{ide-documento}")
    public ObservacionesDocumentoDTO listarObservacionesDocumento(@PathParam("ide-documento") final BigInteger ideDocumento) throws BusinessException, SystemException {
        log.info("processing rest request - listar observaciones de un documento");
        return boundary.listarObservacionesDocumento(ideDocumento);
    }
}
