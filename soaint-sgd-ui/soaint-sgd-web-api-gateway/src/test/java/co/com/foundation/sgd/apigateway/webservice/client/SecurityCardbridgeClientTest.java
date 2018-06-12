package co.com.foundation.sgd.apigateway.webservice.client;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemExceptionException;
import co.com.foundation.test.rules.PropertiesLoaderRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = "/spring/core-config.xml"
)
public class SecurityCardbridgeClientTest {

    @Rule
    public PropertiesLoaderRule properties = PropertiesLoaderRule.from("sgd-service-test.properties");

    @Autowired
    private SecurityCardbridgeClient securityCardbridgeClient;

    private String WSDL_URL = properties.get("proxy.securitycardbridge.endpoint");


    @Test
    public void verifyCredentials() throws SystemExceptionException {
        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";
        ReflectionTestUtils.setField(securityCardbridgeClient, "securitycardbridgeWsdlEndpoint", WSDL_URL);

        // when
        AuthenticationResponseContext context = securityCardbridgeClient.verifyCredentials(LOGIN, PASSWORD);

        // then
        assertThat(context).isNull();
    }

    @Test
    public void verifyCredentialsFail() {
        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";
        String BAD_URL = "BAD_URL";
        ReflectionTestUtils.setField(securityCardbridgeClient, "securitycardbridgeWsdlEndpoint", BAD_URL);

        // when
        assertThatExceptionOfType(SystemExceptionException.class)
                .isThrownBy(() -> securityCardbridgeClient.verifyCredentials(LOGIN, PASSWORD));
    }
}