/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    
}
