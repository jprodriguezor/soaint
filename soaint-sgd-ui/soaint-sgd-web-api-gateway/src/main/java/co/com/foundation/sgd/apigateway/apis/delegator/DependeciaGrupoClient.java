package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class DependeciaGrupoClient {

	@Value( "${productos.backapi.endpoint.url}" )
	private String endpoint = "";

	public DependeciaGrupoClient() {
		super();
	}

	public Response list() {
		System.out.println("DependeciaGrupo - [trafic] - listing DependeciaGrupo with endpoint: " + endpoint);
		WebTarget wt = ClientBuilder.newClient().target(endpoint);
		return wt.path("/dependencia-grupo-web-api")
				.request()
				.get();
	}

}
