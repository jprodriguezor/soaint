import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.FactoriaContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasiel on 30/05/2017.
 */
public class Main {

    public static void main(String[] args) {
        ContentControl a = FactoriaContent.getContentControl("alfresco");
        ContentManagerMediator content = FactoriaContent.getContentManager("alfresco");
        Long ideOrgaAdmin= new Long (1);
        String codOrg="0001";
        String nomOrg="Raiz";
        String tipo="Tipo1";

        OrganigramaDTO orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);

        List<OrganigramaDTO>  organigramaItemList = new ArrayList <> ();
        organigramaItemList.add (orgDTO);


        String idOrgAdm="001";
        String idOrgOfc="0001";
        String codSerie="S001";
        String nomSerie="NS001";
        String codSubSerie="CD001";
        String nomSubSerie="NSS0001";
        Long retArchivoGestion=new Long (123);
        Long retArchivoCentral=new Long (23);
        String procedimiento="Insercion";
        int diposicionFinal=4;
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO=new ContenidoDependenciaTrdDTO(idOrgAdm,idOrgOfc,codSerie,nomSerie,codSubSerie,
                nomSubSerie,retArchivoGestion,retArchivoCentral,procedimiento,diposicionFinal);

        List<ContenidoDependenciaTrdDTO> contenidoDependenciaList=new ArrayList <> ();
        contenidoDependenciaList.add (contenidoDependenciaTrdDTO);
        EstructuraTrdDTO estructuraTrdDTO=new EstructuraTrdDTO (organigramaItemList,contenidoDependenciaList);
        List<EstructuraTrdDTO> structure=new ArrayList <> ();

        structure.add (estructuraTrdDTO);

        MensajeRespuesta b= content.crearEstructuraContent (structure);
    }
}
