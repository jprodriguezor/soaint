package rules;

import co.com.soaint.bpm.services.util.SystemParameters;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.*;

public class ConnectionRule implements TestRule {


    public ConnectionRule() {

        if(isLocal()) {
            System.setProperty(SystemParameters.BUSINESS_PLATFORM_ENDPOINT, "192.168.3.242:28080");
        }
    }

    public Statement apply(Statement statement, Description description) {return statement;}

    private boolean isLocal() {
        return !System.getProperty("user.dir").startsWith("/var/lib/jenkins");
    }

}
