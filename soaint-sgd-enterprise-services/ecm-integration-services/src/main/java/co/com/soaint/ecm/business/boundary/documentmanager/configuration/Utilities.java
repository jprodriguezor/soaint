/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;

import java.util.Comparator;
import java.util.List;

/**
 * @author ADMIN
 */
public class Utilities {

    public Utilities() {
    }

    /**
     * Metodo que dada una lista de entrada la organiza por ideOrgaAdmin
     *
     * @param organigramaItem Lista a ordenar
     */
    public void ordenarListaOrganigrama(List <OrganigramaDTO> organigramaItem) {

        organigramaItem.sort (Comparator.comparing (OrganigramaDTO::getIdeOrgaAdmin));
    }


}
