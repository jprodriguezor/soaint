package co.com.soaint.correspondencia.integration.service.rest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

import co.com.soaint.correspondencia.domain.entity.ValidationError;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        List<ValidationError> errors = exception.getConstraintViolations().stream()
                .map(this::toValidationError)
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST).entity(errors)
                .type(MediaType.APPLICATION_JSON).build();
    }

    private ValidationError toValidationError(ConstraintViolation constraintViolation) {

        String path = constraintViolation.getPropertyPath().toString();
        String clase = constraintViolation.getRootBean().getClass().getSimpleName().split("\\$", -1)[0];
        String paquete = constraintViolation.getRootBean().getClass().getPackage().getName();
        String value = "";// mal
        return ValidationError.newInstance()
                .message(constraintViolation.getMessage())
                .field(path.split("\\.", -1)[2])
                .clase(clase)
                .metodo(path.split("\\.", -1)[0])
                .preparedMessage(this.prepareMessage(clase, path, paquete, value))
                .paquete(paquete)
                .value(value)
                .build();
    }

    private String prepareMessage(String clase, String path, String paquete, String value) {
        if (path == null) return "No validation error set.";
        else if (clase == null ) clase = "No class error set.";
        String[] parsePath = path.split("\\.", -1);
        return paquete + ": " + clase + ": " + "Error obteniendo el " + parsePath[2] + " en el metodo " + parsePath[0] + " con valor " + value;
    }
}