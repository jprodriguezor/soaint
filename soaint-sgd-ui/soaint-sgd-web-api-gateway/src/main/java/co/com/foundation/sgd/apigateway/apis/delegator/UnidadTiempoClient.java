package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class UnidadTiempoClient {

	@Value( "${productos.backapi.endpoint.url}" )
	private String endpoint = "";

	public UnidadTiempoClient() {
		super();
	}

	public Response list() {
		System.out.println("Unidad de Tiempo - [trafic] - listing UnidadTiempo with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/unidad-tiempo-web-api")
				.request()
				.get();
	}

}
