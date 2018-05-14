package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Arce on 6/9/2017.
 */
@Getter
@AllArgsConstructor
public enum EstadosEnum {

    CREADO("Created"), LISTO("Ready"), RESERVADO("Reserved"), ENPROGRESO("InProgress"), SUSPENDIDO("Suspended"), COMPLETADO("Completed"), FALLIDO("Failed"), ERROR("Error"), SALIDO("Exited"), OBSOLETO("Obsolete");

    private final String nombre;


    public static EstadosEnum obtenerClave(String nombre){

        for (EstadosEnum valor : EstadosEnum.values()) {
            if (valor.getNombre().equalsIgnoreCase(nombre)) {
                return valor;
            }
        }
        return null;
    }

}
