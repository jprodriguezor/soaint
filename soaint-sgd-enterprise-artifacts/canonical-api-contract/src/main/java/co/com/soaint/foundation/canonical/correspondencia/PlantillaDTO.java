package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:11-Ene-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/plantilla/1.0.0")
public class PlantillaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idePlantilla;
    private String referencia;
    private String codClasificacion;
    private String codTipoUbicacion;
    private String ubicacion;
    private String estado;
    private PlantillaMetadatosDTO metadatos;

    public PlantillaDTO(BigInteger idePlantilla, String referencia, String codClasificacion,
                        String codTipoUbicacion, String ubicacion){
        this.idePlantilla = idePlantilla;
        this.referencia = referencia;
        this.codClasificacion = codClasificacion;
        this.codTipoUbicacion = codTipoUbicacion;
        this.ubicacion = ubicacion;
    }
}
