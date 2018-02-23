package co.com.soaint.foundation.canonical.ecm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/contenidoDependencia/1.0.0")
public class ContenidoDependenciaTrdDTO {

    private String idOrgAdm;
    private String idOrgOfc;
    private String codSerie;
    private String nomSerie;
    private String codSubSerie;
    private String nomSubSerie;
    private Long retArchivoGestion;
    private Long retArchivoCentral;
    private String procedimiento;
    private int diposicionFinal;
    private List<SerieDTO> listaSerie;
    private List<SubSerieDTO> listaSubSerie;


}