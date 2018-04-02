/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;

/**
 * @author ADMIN
 */
public class Utilities {

    public Utilities() {
        /**/
    }

    /**
     * Metodo que dada una lista de entrada la organiza por ideOrgaAdmin
     *
     * @param organigramaItem Lista a ordenar
     */
    public void ordenarListaOrganigrama(List <OrganigramaDTO> organigramaItem) {

        organigramaItem.sort (Comparator.comparing (OrganigramaDTO::getIdeOrgaAdmin));
    }

    /**
     * Función que elimina acentos y caracteres especiales de
     * una cadena de texto.
     * @param texto
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String reemplazarCaracteresRaros(String texto) {
        String resultText = Normalizer.normalize(texto, Normalizer.Form.NFD);
        resultText = resultText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return resultText;
    }
}
