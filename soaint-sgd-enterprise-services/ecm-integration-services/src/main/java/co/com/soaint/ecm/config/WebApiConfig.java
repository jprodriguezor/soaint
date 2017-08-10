package co.com.soaint.ecm.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Contexto del servicio
 */
@ApplicationPath("/apis")
public class WebApiConfig extends Application {
    /**
     * Constructor de la clase
     */
    public WebApiConfig() {
    }

}