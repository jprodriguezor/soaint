package co.com.foundation.sgd.apigateway.rules;

import co.com.foundation.sgd.utils.SystemParameters;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

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

        }
    }

    private boolean isPropertiesSet() {
        return System.getProperty(SystemParameters.BACKAPI_ENDPOINT_URL) != null;
    }
}
