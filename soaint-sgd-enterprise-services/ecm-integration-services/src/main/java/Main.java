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
        Long ideOrgaAdmin= new Long (321);
        String codOrg="000";
        String nomOrg="SOAINT";
        String tipo="P";

        OrganigramaDTO orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        List<OrganigramaDTO>  organigramaItemList = new ArrayList <> ();

        organigramaItemList.add (orgDTO);

        ideOrgaAdmin= new Long (322);
        codOrg="100";
        nomOrg="100_PRESIDENCIA";
        tipo="H";
        orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        organigramaItemList.add (orgDTO);

        ideOrgaAdmin= new Long (325);
        codOrg="100.120";
        nomOrg="100.120_OFICINA NACIONAL DE CONTROL DISCIPLINARIO INTERNO";
        tipo="H";
        orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        organigramaItemList.add (orgDTO);

        String idOrgAdm="100";
        String idOrgOfc="100.100";
        String codSerie="20";
        String nomSerie="ACTAS";
        String codSubSerie="2";
        String nomSubSerie="Actas comité de conciliación y defensa judicial";
        Long retArchivoGestion=new Long (4);
        Long retArchivoCentral=new Long (1);
        String procedimiento="Conservar los expedientes";
        int diposicionFinal=2;
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
