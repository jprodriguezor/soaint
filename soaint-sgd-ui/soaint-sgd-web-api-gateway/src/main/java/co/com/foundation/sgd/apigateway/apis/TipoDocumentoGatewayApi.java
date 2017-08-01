package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoDocumentoClient;
import co.com.foundation.sgd.apigateway.apis.delegator.UnidadTiempoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tipo-documento-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoDocumentoGatewayApi {

	@Autowired
	private TipoDocumentoClient client;

	public TipoDocumentoGatewayApi() {
		super();
		 SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@GET
	@Path("/")
	@JWTTokenSecurity
	public Response list() {
		//TODO: add trafic log
		System.out.println("TipoDocumentoGatewayApi - [trafic] - listing TipoDocumento");
		Response response = client.list();
		String responseContent = response.readEntity(String.class);
		System.out.println("TipoDocumentoGatewayApi - [content] : " + responseContent);
		
		return Response.status( response.getStatus() ).entity(responseContent).build();
	}

}
