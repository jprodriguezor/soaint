package co.com.foundation.rules;

import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Log4j2
public class EnvironmentRule implements TestRule {

    private final String PROPERTIES_SOURCE = "endpoints.properties";

    public EnvironmentRule() {
        initProperties();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return base;
    }

    private void initProperties() {

        if(isPropertiesSet()) return;

        try {
            PropertiesLoaderUtils
                    .loadAllProperties(PROPERTIES_SOURCE)
                    .forEach((key, value) -> System.setProperty(key.toString(), value.toString()));

        } catch (Exception e) {
            log.error("Error al cargar el fichero 'endpoints.properties' para ejecutar pruebas locales");
        }
    }

    private boolean isPropertiesSet() {
        return System.getProperty(SystemParameters.BACKAPI_ENDPOINT_URL) != null;
    }
}
