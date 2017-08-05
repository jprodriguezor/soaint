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
        String codOrg="000";
        String nomOrg="000_SOAINT";
        String tipo="P";

        OrganigramaDTO orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        List<OrganigramaDTO>  organigramaItemList = new ArrayList <> ();

        organigramaItemList.add (orgDTO);

        ideOrgaAdmin= new Long (35);
        codOrg="100";
        nomOrg="100_PRESIDENCIA";
        tipo="H";
        orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        organigramaItemList.add (orgDTO);

        ideOrgaAdmin= new Long (36);
        codOrg="100110";
        nomOrg="100110_OFICINA NACIONAL DE CONTROL DISCIPLINARIO INTERNO";
        tipo="H";
        orgDTO=new OrganigramaDTO (ideOrgaAdmin,codOrg,nomOrg,tipo);
        organigramaItemList.add (orgDTO);

        List<ContenidoDependenciaTrdDTO> contenidoDependenciaList=new ArrayList <> ();

        String idOrgAdm="100";
        String idOrgOfc="100110";
        String codSerie="00300";
        String nomSerie="PAGARE";
        String codSubSerie="001000";
        String nomSubSerie="DOCUMENTOS VITALES";
        Long retArchivoGestion=new Long (3);
        Long retArchivoCentral=new Long (2);
        String procedimiento="Definir procedimiento en su momento.";
        int diposicionFinal=1;
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO=new ContenidoDependenciaTrdDTO(idOrgAdm,idOrgOfc,codSerie,nomSerie,codSubSerie,
                nomSubSerie,retArchivoGestion,retArchivoCentral,procedimiento,diposicionFinal);
        contenidoDependenciaList.add (contenidoDependenciaTrdDTO);

        idOrgAdm="100";
        idOrgOfc="100110";
        codSerie="0056";
        nomSerie="INFORMES";
        codSubSerie="";
        nomSubSerie="";
        retArchivoGestion=new Long (5);
        retArchivoCentral=new Long (3);
        procedimiento="Definir procedimiento.";
        diposicionFinal=2;
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO1=new ContenidoDependenciaTrdDTO(idOrgAdm,idOrgOfc,codSerie,nomSerie,codSubSerie,
                nomSubSerie,retArchivoGestion,retArchivoCentral,procedimiento,diposicionFinal);
        contenidoDependenciaList.add (contenidoDependenciaTrdDTO1);


        idOrgAdm="100";
        idOrgOfc="100110";
        codSerie="1100";
        nomSerie="PLANES BANCO";
        codSubSerie="1101";
        nomSubSerie="Vacaciones";
        retArchivoGestion=new Long (6);
        retArchivoCentral=new Long (4);
        procedimiento="Definición de procedimiento pendiente.";
        diposicionFinal=2;
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO3=new ContenidoDependenciaTrdDTO(idOrgAdm,idOrgOfc,codSerie,nomSerie,codSubSerie,
                nomSubSerie,retArchivoGestion,retArchivoCentral,procedimiento,diposicionFinal);
        contenidoDependenciaList.add (contenidoDependenciaTrdDTO3);



        EstructuraTrdDTO estructuraTrdDTO=new EstructuraTrdDTO (organigramaItemList,contenidoDependenciaList);
        List<EstructuraTrdDTO> structure=new ArrayList <> ();

        structure.add (estructuraTrdDTO);

        String idDoc=content.subirDocumentoContent ("TP-CMCOE","C:\\Users\\Dasiel\\Desktop\\alice.pdf","alice.pdf","daiel","Alicia en el Pais de las Maravillas","Descripcion doc");

 //       MensajeRespuesta b= content.crearEstructuraContent (structure);

    }
}
