package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class TipologiaDocumentalClient {

	@Value( "${productos.backapi.endpoint.url}" )
	private String endpoint = "";

	public TipologiaDocumentalClient() {
		super();
	}

	public Response list() {
		System.out.println("TipologiaDocumental - [trafic] - listing TipologiaDocumental with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/tipologia-documental-web-api")
				.request()
				.get();
	}

}
