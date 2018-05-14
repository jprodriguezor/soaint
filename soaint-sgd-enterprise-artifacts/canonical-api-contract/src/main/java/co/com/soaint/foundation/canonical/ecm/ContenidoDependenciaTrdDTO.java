package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
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
@ToString
public class ContenidoDependenciaTrdDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idOrgAdm;
    private String idOrgOfc;
    private String codSede;
    private String codSerie;
    private String nomSerie;
    private String codSubSerie;
    private String nomSubSerie;
    private Long retArchivoGestion;
    private Long retArchivoCentral;
    private String procedimiento;
    private int diposicionFinal;

    private List<SubSerieDTO> listaSubSerie;
    private List<SerieDTO> listaSerie;
    private List<OrganigramaDTO> listaDependencia;
    private List<SedeDTO> listaSede;

}