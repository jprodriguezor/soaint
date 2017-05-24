package co.com.soaint.correspondencia.integration.service.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created by esanchez on 5/24/2017.
 */

@ApplicationPath("/restApi")
public class ConfigApp extends Application {
    public ConfigApp() {
    }
}