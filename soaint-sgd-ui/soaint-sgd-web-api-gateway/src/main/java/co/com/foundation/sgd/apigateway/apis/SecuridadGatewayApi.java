package co.com.foundation.sgd.apigateway.apis;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import co.com.foundation.sgd.apigateway.webservice.client.SecurityCardbridgeClient;
import org.apache.commons.lang3.StringUtils;

import co.com.foundation.sgd.infrastructure.KeyManager;
import co.com.soaint.foundation.canonical.seguridad.UsuarioDTO;

@Path("/securidad-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SecuridadGatewayApi {

    @Context
    private UriInfo uriInfo;

    public SecuridadGatewayApi() {
        super();
    }

    @POST
    @Path("/login")
    public Response login(final UsuarioDTO user) {

        try {

            // Authenticate the user using the credentials provided
            System.out.println("calling login service");

            SecurityCardbridgeClient.verifyCredentials(user.getLogin(), user.getPassword());

            if (StringUtils.equals(user.getLogin(), "soaint") && StringUtils.equals(user.getPassword(), "123")) {

                // Issue a token for the user
                KeyManager km = KeyManager.getInstance();
                String token = km.issueToken(user.getLogin(), uriInfo.getAbsolutePath().toString());

                // Return the token on the response
                return Response.ok("{ \"token\": \"" + token + "\" }").header(AUTHORIZATION, "Bearer " + token).build();
            } else {
                return Response.status(UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

}
