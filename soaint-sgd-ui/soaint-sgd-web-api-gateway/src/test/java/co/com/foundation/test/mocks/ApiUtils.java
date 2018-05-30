package co.com.foundation.test.mocks;

import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import org.assertj.core.api.Assertions;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiUtils {

    public static <T> ApiAssertions<T> assertThat(Class<T> type, String methodName) {
        return new ApiAssertions<T>(type, methodName);
    }

    public static class ApiAssertions<T> {

        private final Class<T> controllerClass;

        private final Method method;


        public ApiAssertions(Class<T> controllerClass, String methodName) {

            this.controllerClass = controllerClass;

            this.method =  Arrays.asList(controllerClass.getDeclaredMethods()).stream()
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El método " + methodName + " no existe en la clase " + controllerClass.getName() + "."));

            if(!method.isAnnotationPresent(Path.class))
                throw new RuntimeException("El método " + method.getName() + " no es un método del controller válido. Verifique si possee la annotación @Path.");
        }

        public ApiAssertions<T> produces(String ...mediaTypes) {
            Produces annotation = assertAnnotation(Produces.class);

            Assertions.assertThat(annotation.value())
                    .as("Produces media type")
                    .containsOnly(mediaTypes);

            return this;
        }

        public ApiAssertions<T> consumes(String ...mediaTypes) {
            Consumes annotation = assertAnnotation(Consumes.class);

            Assertions.assertThat(annotation.value())
                    .as("Consumes media type")
                    .containsOnly(mediaTypes);

            return this;
        }

        public ApiAssertions<T> hasJWTSecurity() {

            assertAnnotation(JWTTokenSecurity.class);

            return this;
        }

        public ApiAssertions<T> hasGetMapping(String path) {

            assertAnnotation(GET.class);

            assertPath(path);

            return this;
        }

        public ApiAssertions<T> hasPostMapping(String path) {

            assertAnnotation(POST.class);

            assertPath(path);

            return this;
        }

        public ApiAssertions<T> hasPutMapping(String path) {

            assertAnnotation(PUT.class);

            assertPath(path);

            return this;
        }

        public ApiAssertions<T> hasDeleteMapping(String path) {

            assertAnnotation(DELETE.class);

            assertPath(path);

            return this;
        }

        private void assertPath(String path) {

            String thePath = getAnnotations(Path.class).stream()
                    .map(Path::value)
                    .filter(p -> !p.equals("/"))
                    .collect(Collectors.joining());

            Assertions.assertThat(thePath)
                    .as("Path mismatch")
                    .isEqualTo(path);
        }

        private <A extends Annotation> List<A> getAnnotations(Class<A> annotation) {

            List<A> annotations = new ArrayList<>();

            if(controllerClass.isAnnotationPresent(annotation))
                annotations.add(controllerClass.getAnnotation(annotation));

            annotations.add(method.getAnnotation(annotation));

            return annotations;
        }

        private <A extends Annotation> A assertAnnotation(Class<A> annotation) {

            Assertions.assertThat(method.isAnnotationPresent(annotation) || controllerClass.isAnnotationPresent(annotation))
                    .as("Method %s should has the annotation %s", method.getName(), annotation.getName())
                    .isTrue();

            return method.isAnnotationPresent(annotation)
                    ? method.getAnnotation(annotation)
                    : controllerClass.getAnnotation(annotation);
        }
    }

}
