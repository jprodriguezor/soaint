package co.com.foundation.sgd.apigateway.apis;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import co.com.foundation.sgd.apigateway.apis.delegator.ProductosClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;

@Path("/productos-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProductosGatewayApi {

	@Autowired
	private ProductosClient productosClient;
	
	public ProductosGatewayApi() {
		super();
		 SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@GET
	@Path("/")
	@JWTTokenSecurity
	public Response list() {

		log.info("ProductosGatewayApi - [trafic] - listing products");
		Response response = productosClient.list();
		String responseContent = response.readEntity(String.class);
		log.info("ProductosGatewayApi - [content] : " + responseContent);
		
		return Response.status( response.getStatus() ).entity(responseContent).build();
	}

}
