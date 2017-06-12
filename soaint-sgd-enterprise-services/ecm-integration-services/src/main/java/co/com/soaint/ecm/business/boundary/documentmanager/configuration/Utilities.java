/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * @author ADMIN
 */
public class Utilities {
    
    public Utilities(){}
    
    public void ordenarListaOrganigrama(List<OrganigramaDTO> organigramaItem) {

        Collections.sort(organigramaItem, new Comparator<OrganigramaDTO>() {

            @Override
            public int compare(OrganigramaDTO o1, OrganigramaDTO o2) {
                return o1.getIdeOrgaAdmin().compareTo(o2.getIdeOrgaAdmin());
            }
        });        
    }

    /**
     * Método que, dado el nombre de un espacio, devuelve su Id (nodeRef)
     * @param session
     * @param nombre
     * @return nodeRef (id)
     */
    public String obtenerIdDocumento(Session session, String nombre){

        String queryString = "SELECT cmis:objectId FROM cmis:document WHERE cmis:name = '" + nombre+"'";

        ItemIterable<QueryResult> results = session.query(queryString, false);

        String id = "";

        for (QueryResult qResult : results) {
            String objectId = qResult.getPropertyValueByQueryName("cmis:objectId");
            Document document = (Document) session.getObject(session.createObjectId(objectId));
            id = document.getId();
            System.out.println("Listado de carpetas: "+document.getId());
        }
        return id;

    }

    /**
     * Método que, dado la sesión y el nombre del documento, devuelve su contenido (InputStream)
     * @param session
     * @param nombre
     * @return InputStream(stream)
     */
    public InputStream obtenerContenidoDocumento(Session session, String nombre){
        CmisObject object = session.getObject(session.createObjectId(obtenerIdDocumento (session,nombre)));
        Document document;
        InputStream stream=null;
        if (object instanceof Document){
            document = (Document) object;
            String filename = document.getName();
            stream = document.getContentStream().getStream();
        }
        else{
            System.out.println("No es de tipo document");
        }
        return stream;
    }



    
}
