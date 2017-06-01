package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class PaisClient {

	@Value("${backapi.endpoint.url}")
	private String endpoint = "";

	public PaisClient() {
		super();
	}

	public Response list() {
		System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/pais-web-api")
				.request()
				.get();
	}

}
