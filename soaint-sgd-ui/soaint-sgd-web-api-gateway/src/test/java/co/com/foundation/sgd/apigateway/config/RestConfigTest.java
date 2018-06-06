package co.com.foundation.sgd.apigateway.config;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

public class RestConfigTest {

    @Test
    public void shouldSetMainPath() {

        RestConfig restConfig = new RestConfig();

        Assertions.assertThat(RestConfig.class.getSuperclass()).isEqualTo(Application.class);

        Assertions.assertThat(RestConfig.class.isAnnotationPresent(ApplicationPath.class)).isTrue();

        ApplicationPath applicationPath = RestConfig.class.getAnnotation(ApplicationPath.class);

        Assertions.assertThat(applicationPath.value()).isEqualTo("/apis");
    }
}