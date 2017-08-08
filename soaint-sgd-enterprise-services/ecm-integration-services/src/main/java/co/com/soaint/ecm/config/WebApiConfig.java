package co.com.soaint.ecm.config;

/**
 * Created by Dasiel on 7/08/2017.
 */

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/apis")
public class WebApiConfig extends Application {

    public WebApiConfig() {
    }

}