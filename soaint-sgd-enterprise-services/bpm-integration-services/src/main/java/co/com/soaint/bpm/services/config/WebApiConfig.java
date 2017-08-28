package co.com.soaint.bpm.services.config;

/**
 * Created by arce on 7/06/2017.
 */

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Path para el apis
 */
@ApplicationPath("/apis")
public class WebApiConfig extends Application {

    public WebApiConfig() {
        //
    }

}