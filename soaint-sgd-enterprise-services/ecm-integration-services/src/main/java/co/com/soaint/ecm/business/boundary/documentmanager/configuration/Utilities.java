/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.configuration;

import co.com.foundation.soaint.documentmanager.domain.OrganigramaVO;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class Utilities {
    
    public Utilities(){}
    
    public void ordenarListaOrganigrama(List<OrganigramaVO> organigramaItem) {

        Collections.sort(organigramaItem, new Comparator<OrganigramaVO>() {

            @Override
            public int compare(OrganigramaVO o1, OrganigramaVO o2) {
                return o1.getIdeOrgaAdmin().compareTo(o2.getIdeOrgaAdmin());
            }
        });        
    }
    
}
