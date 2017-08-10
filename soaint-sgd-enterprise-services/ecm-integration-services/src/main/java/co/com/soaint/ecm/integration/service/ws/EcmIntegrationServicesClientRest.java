package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Dasiel
 */

/**
 * Clase que expone las operaciones como servicio Rest
 */
@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcmIntegrationServicesClientRest {

    private static final Logger logger = LogManager.getLogger (EcmIntegrationServicesClientRest.class.getName ( ));

    @Autowired
    private
    EcmManagerMediator fEcmManager;

    /**
     * Constructor de la clase
     */
    public EcmIntegrationServicesClientRest() {

        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);
    }

    /**
     * Operacion para crear la estructura dentro del ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws BusinessException Error de negocio
     * @throws SystemException   Excepcion en error
     */
    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) throws BusinessException, SystemException {
        logger.info ("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM (structure);
        } catch (Exception e) {
            e.printStackTrace ( );
            logger.error ("Error servicio creando estructura " + e);
            throw e;
        }
    }

    /**
     * Operacion que sube documentos al ECM
     *
     * @param nombreDocumento  Nombre del documento
     * @param documento        Documento
     * @param tipoComunicacion Tipo de comunicacion puede ser externa o interna
     * @return Identificador del documento creado
     * @throws BusinessException Error de negocio
     * @throws SystemException   Excepcion en error
     */
    @POST
    @Path("/subirDocumentoECM/{nombreDocumento}/{tipoComunicacion}")
    @Consumes("multipart/form-data")
    public String subirDocumentoECM(@PathParam("nombreDocumento") String nombreDocumento,
                                    @RequestPart("documento") final MultipartFormDataInput documento,
                                    @PathParam("tipoComunicacion") String tipoComunicacion) throws BusinessException, SystemException {
        logger.info ("processing rest request - Subir Documento ECM " + nombreDocumento + " " + tipoComunicacion);
        try {

            return fEcmManager.subirDocumento (nombreDocumento, documento, tipoComunicacion);
        } catch (Exception e) {
            e.printStackTrace ( );
            logger.info ("Error en operacion - Subir Documento ECM " + e);
            throw e;

        }

    }

    /**
     * Operacion que que mueve documentos dentro del ECM
     *
     * @param moverDocumento Nombre del documentoa mover
     * @param carpetaFuente  Carpeta donde se encuentra el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws BusinessException Error de negocio
     * @throws SystemException   Excepcion en error
     */
    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam("moverDocumento") final String moverDocumento,
                                              @QueryParam("carpetaFuente") final String carpetaFuente,
                                              @QueryParam("carpetaDestino") final String carpetaDestino) throws BusinessException, SystemException {

        logger.info ("processing rest request - Mover Documento ECM");
        try {
            return fEcmManager.moverDocumento (moverDocumento, carpetaFuente, carpetaDestino);
        } catch (Exception e) {
            e.printStackTrace ( );
            logger.error ("Error servicio moviendo documento " + e);
            throw e;
        }
    }

}

