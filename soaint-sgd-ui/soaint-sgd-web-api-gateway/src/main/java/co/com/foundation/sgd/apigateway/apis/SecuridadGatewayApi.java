package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.webservice.client.SecurityCardbridgeClient;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.dto.AccountDTO;
import co.com.foundation.sgd.infrastructure.KeyManager;
import co.com.soaint.foundation.canonical.seguridad.UsuarioDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/securidad-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class SecuridadGatewayApi {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private SecurityCardbridgeClient securityCardbridgeClient;

    /**
     * Constructor
     */
    public SecuridadGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Accion para logearse en la aplicación
     *
     * @param user Credenciales del usuario
     * @return Response
     */
    @POST
    @Path("/login")
    public Response login(final UsuarioDTO user) {

        try {
            log.info("Authenticate the user using the credentials provided");
            AuthenticationResponseContext context = securityCardbridgeClient.verifyCredentials(user.getLogin(), user.getPassword());

            if (context.isSuccessful()) {
                // Issue a token for the user
                KeyManager km = KeyManager.getInstance();
                String token = km.issueToken(user.getLogin(), uriInfo.getAbsolutePath().toString());
                // Return the token on the response
                AccountDTO account = new AccountDTO(token, context.getPrincipalContext());
                return Response.ok(account).header(AUTHORIZATION, "Bearer " + token).build();
            } else {
                return Response.status(UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            log.error(e);
            return Response.status(UNAUTHORIZED).build();
        }
    }

}
