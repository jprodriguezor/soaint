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

        if(!isLocal()) return;

        if(System.getProperty("java.io.tmpdir") == null)
            System.setProperty("java.io.tmpdir", "/tmp");

        System.setProperty(SystemParameters.BACKAPI_ENDPOINT_URL, "http://192.168.3.242:28080/correspondencia-business-services/services");
        System.setProperty(SystemParameters.BACKAPI_CARGAMASIVA_ENDPOINT_URL, "http://192.168.3.242:28080/Massive-Loader/massiveloaderapi");
        System.setProperty(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/bpm-integration-services/apis");
        System.setProperty(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/ecm-integration-services/apis/ecm");
        System.setProperty(SystemParameters.BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/ecm-integration-services/apis/record");
        System.setProperty(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL, "http://192.168.3.242:28080/funcionario-integration-services/services");
        System.setProperty(SystemParameters.BACKAPI_DROOLS_SERVICE_ENDPOINT_URL, "http://192.168.3.243:28080/kie-server/services/rest/server/containers/instances");
        System.setProperty(SystemParameters.BACKAPI_DROOLS_SERVICE_TOKEN, "a3Jpc3Y6a3Jpc3Y=");
    }

    private boolean isLocal() {
        return !System.getProperty("user.dir").contains("jenkins");
    }
}
