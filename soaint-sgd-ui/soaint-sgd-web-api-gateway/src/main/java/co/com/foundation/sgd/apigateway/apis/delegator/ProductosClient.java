package co.com.foundation.sgd.apigateway.apis.delegator;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;

import co.com.foundation.sgd.infrastructure.ApiDelegator;

@ApiDelegator
public class ProductosClient {

	@Value("${backapi.endpoint.url}")
	private String endpoint = "";
	
	public ProductosClient() {
		super();
	}

	public Response list() {
		System.out.println("ProductosClient - [trafic] - listing products with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/productos-web-api")
				.request()
				.get();
	}

}
