
package co.com.soaint.foundation.canonical.ecm;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
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
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/estructuraTrd/1.0.0")
@ToString
public class EstructuraTrdDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<OrganigramaDTO>  organigramaItemList;
    private List<ContenidoDependenciaTrdDTO> contenidoDependenciaList;
    
   
    public EstructuraTrdDTO(){
        super();
        organigramaItemList =new ArrayList<>();
        contenidoDependenciaList =new ArrayList<>();
    }

    public EstructuraTrdDTO(List<OrganigramaDTO> organigramaItemList, List<ContenidoDependenciaTrdDTO> contenidoDependenciaList) {
        this.contenidoDependenciaList = contenidoDependenciaList;
        this.organigramaItemList = organigramaItemList;
    }
}