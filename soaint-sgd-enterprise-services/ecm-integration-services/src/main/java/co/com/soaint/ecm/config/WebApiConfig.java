package co.com.soaint.ecm.config;

/**
 * Created by Dasiel on 7/08/2017.
 */

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/apis")
public class WebApiConfig extends Application {
    /*
        @Override
        public Set<Class<?>> getClasses() {
            final Set<Class<?>> classes = new HashSet<Class<?>>();
            // register resources and features
            classes.add(MultiPartFeature.class);
            //classes.add(MultiPartResource.class);
            //classes.add(LoggingFilter.class);
            return classes;
        }
    */
    public WebApiConfig() {
    }

}