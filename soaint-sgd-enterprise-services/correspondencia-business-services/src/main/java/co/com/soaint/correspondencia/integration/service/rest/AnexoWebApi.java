package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexosFullDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * Created by gyanet on 3/21/2018.
 */
@Path("/anexo-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "AnexoWebApi", description = "")
public class AnexoWebApi {

    @Autowired
    private GestionarAnexo boundary;

    /**
     * Constructor
     */
    public AnexoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("anexo/{nroRadicado}")
    public AnexosFullDTO listarAnexosPorNroRadicado(@PathParam("nroRadicado") final String nroRadicado) throws SystemException {
        log.info("processing rest request - listar anexos por nroRadicado");
//        AnexoFullDTO anexoFullDTO = new AnexoFullDTO();
//        anexoFullDTO.setCodAnexo("anexoCode");
//        anexoFullDTO.setCodTipoSoporte("codeSop");
//        anexoFullDTO.setDescripcion("Descripcion");
//        anexoFullDTO.setDescTipoAnexo("DescTipoAnexo");
//        anexoFullDTO.setDescTipoSoporte("DescTipoSoporte");
//        anexoFullDTO.setIdeAnexo(new BigInteger("200"));
//
//        AnexosFullDTO anexosFullDTO = new AnexosFullDTO();
//        anexosFullDTO.setAnexos(new ArrayList<>());
//        anexosFullDTO.getAnexos().add(anexoFullDTO);
        return boundary.listarAnexosByNroRadicado(nroRadicado);
    }


}
