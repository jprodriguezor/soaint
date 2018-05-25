package co.com.foundation.sgd.apigateway.rules;

import co.com.foundation.sgd.utils.SystemParameters;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Properties;

public class EnvironmentRule implements TestRule {

    public EnvironmentRule() {
        initProperties();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return base;
    }

    private void initProperties() {
        Properties props = new Properties();

        props.put("java.io.tmpdir", "file://tmp/jaxrs/");
        props.put(SystemParameters.BACKAPI_ENDPOINT_URL, "http://192.168.3.242:28080/correspondencia-business-services/services");
        props.put(SystemParameters.BACKAPI_CARGAMASIVA_ENDPOINT_URL, "http://192.168.3.242:28080/Massive-Loader/massiveloaderapi");
        props.put(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/bpm-integration-services/apis");
        props.put(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/ecm-integration-services/apis/ecm");
        props.put(SystemParameters.BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/ecm-integration-services/apis/record");
        props.put(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/funcionario-integration-services/services");
        props.put(SystemParameters.BACKAPI_DROOLS_SERVICE_ENDPOINT_URL, "http://192.168.3.243:28080/kie-server/services/rest/server/containers/instances");
        props.put(SystemParameters.BACKAPI_DROOLS_SERVICE_TOKEN, "a3Jpc3Y6a3Jpc3Y=");

        System.setProperties(props);
    }
}
