package co.com.soaint.correspondencia.config;

/**
 * Created by jrodriguez on 24/05/2017.
 */

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/apis")
public class WebApiConfig extends Application {
	
    public WebApiConfig() {
    }
    
}