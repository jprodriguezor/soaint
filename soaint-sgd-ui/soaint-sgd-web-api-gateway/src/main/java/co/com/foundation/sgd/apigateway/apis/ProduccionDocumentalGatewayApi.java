package co.com.foundation.sgd.apigateway.apis;

import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/produccion-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProduccionDocumentalGatewayApi {

    private String endpoint = "";

    public ProduccionDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/ejecutar-proyeccion-multiple")
    //@JWTTokenSecurity
    public void ejecutarProyeccionMultiple(TareaDTO tarea) {

        log.info("ProduccionDocumentalGatewayApi - [trafic] - get task variables");
        log.info(tarea);
        log.info("ENDED");
    }
}
