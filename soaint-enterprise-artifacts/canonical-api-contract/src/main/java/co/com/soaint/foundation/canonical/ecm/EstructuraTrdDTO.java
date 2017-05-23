
package co.com.soaint.foundation.canonical.ecm;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
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
public class EstructuraTrdDTO {

    private List<OrganigramaDTO>  organigramaItemList;
    private List<ContenidoDependenciaTrdDTO> contenidoDependenciaList;
    
   
    public EstructuraTrdDTO(){
        super();
        organigramaItemList =new ArrayList<>();
        contenidoDependenciaList =new ArrayList<>();
    }

    public EstructuraTrdDTO(List<ContenidoDependenciaTrdDTO> contenidoDependenciaList, List<OrganigramaDTO> organigramaItemList) {
        this.contenidoDependenciaList = contenidoDependenciaList;
        this.organigramaItemList = organigramaItemList;
    }
}