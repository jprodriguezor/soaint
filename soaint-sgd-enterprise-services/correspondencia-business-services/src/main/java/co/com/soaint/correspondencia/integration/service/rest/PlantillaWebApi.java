package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarPlantilla;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 12-Ene-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/plantilla-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "PlantillaWebApi")
public class PlantillaWebApi {

    @Autowired
    GestionarPlantilla boundary;

    /**
     * Constructor
     */
    public PlantillaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param codTipoDoc
     * @param estado
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @GET
    @Path("/plantilla/{cod_tipo_doc}/{estado}")
    public PlantillaDTO consultarPlantillaByCodClasificacionAndEstaddo(@PathParam("cod_tipo_doc") final String codTipoDoc,
                                                                       @PathParam("estado") final String estado)throws SystemException, BusinessException{
        log.info("processing rest request - listar plantilla por codigo de clasificacion y estado");
        return boundary.consultarPlantillaByCodClasificacionAndEstaddo(codTipoDoc, estado);
    }

    @GET
    @Path("/plantilla/tipologias_doc/{estado}")
    public List<ConstanteDTO> listarTipologiasDocumentalesByEstado(@PathParam("estado") String estado)throws SystemException{
        return boundary.listarTipologiasDocumentalesByEstado(estado);
    }

}
