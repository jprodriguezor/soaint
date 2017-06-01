package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class TipoDocumentacionClient {

	@Value( "${productos.backapi.endpoint.url}" )
	private String endpoint = "";

	public TipoDocumentacionClient() {
		super();
	}

	public Response list() {
		System.out.println("TipoDocumentacion - [trafic] - listing TipoDocumentacion with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/tipo-documentacion-web-api")
				.request()
				.get();
	}

}
