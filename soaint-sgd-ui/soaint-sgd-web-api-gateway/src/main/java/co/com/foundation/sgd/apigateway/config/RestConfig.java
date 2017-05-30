package co.com.foundation.sgd.apigateway.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/apis")
public class RestConfig extends Application {

	public RestConfig() {
		super();
	}

}
