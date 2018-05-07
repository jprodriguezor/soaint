package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValidationError {
    private String path;
    private String message;
    private String field;
    private String clase;
    private String metodo;
    private String preparedMessage;
    private String paquete;
    private String value;
}
