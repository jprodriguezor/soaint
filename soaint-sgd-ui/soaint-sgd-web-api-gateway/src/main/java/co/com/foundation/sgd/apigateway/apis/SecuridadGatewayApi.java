package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.webservice.client.SecurityCardbridgeClient;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.dto.AccountDTO;
import co.com.foundation.sgd.infrastructure.KeyManager;
import co.com.soaint.foundation.canonical.seguridad.UsuarioDTO;
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
public class SecuridadGatewayApi {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private SecurityCardbridgeClient securityCardbridgeClient;

    public SecuridadGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/login")
    public Response login(final UsuarioDTO user) {

        try {
            // Authenticate the user using the credentials provided
            System.out.println("calling login service");
            System.out.println(securityCardbridgeClient);

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
            return Response.status(UNAUTHORIZED).build();
        }
    }

}
