package co.com.soaint.ecm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by amartinez on 14/02/2018.
 */
@Getter
@AllArgsConstructor
public enum DiposicionFinalEnum {
    RETENER("0", "retain"),
    CONSERVACION_TOTAL("1", "transfer"),
    PORDEFINIR("3", "denifir"),
    ELIMINACION("2", "destroy"),
    INTERRUMPIR("4", "cutoff"),
    INCORPORACION("5", "accession");


    private final String codigo;
    private final String nombre;

    public static DiposicionFinalEnum obtenerClave(String codigo) {
        DiposicionFinalEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DiposicionFinalEnum valor = var1[var3];
            if(valor.getCodigo().equalsIgnoreCase(codigo)) {
                return valor;
            }
        }

        return null;
    }

}

