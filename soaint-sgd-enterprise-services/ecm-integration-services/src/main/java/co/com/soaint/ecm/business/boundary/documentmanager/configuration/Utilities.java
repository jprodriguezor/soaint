/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Calendar;
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
    public void ordenarListaOrganigrama(List<OrganigramaDTO> organigramaItem) {

        organigramaItem.sort(Comparator.comparing(OrganigramaDTO::getIdeOrgaAdmin));
    }

    /**
     * Función que elimina acentos y caracteres especiales de
     * una cadena de texto.
     *
     * @param texto
     * @return cadena de texto limpia de acentos y caracteres especiales.
     */
    public static String reemplazarCaracteresRaros(String texto) {
        String resultText = Normalizer.normalize(texto, Normalizer.Form.NFD);
        resultText = resultText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return resultText;
    }

    /**
     * Metodo para comparar fechas
     *
     * @param dtoCalendar {@link Calendar}
     * @param folderCalendar {@link Calendar}
     * @return int {@link Integer}
     */
    public static int comparaFecha(Calendar dtoCalendar, Calendar folderCalendar) {
        LocalDate dtoDate = LocalDate.of(dtoCalendar.get(Calendar.YEAR),
                dtoCalendar.get(Calendar.MONTH) + 1, dtoCalendar.get(Calendar.DAY_OF_MONTH));
        LocalDate folderDate = LocalDate.of(folderCalendar.get(Calendar.YEAR),
                folderCalendar.get(Calendar.MONTH) + 1, folderCalendar.get(Calendar.DAY_OF_MONTH));
        return dtoDate.compareTo(folderDate);
    }

    public static Calendar sumarDiasAFecha(Calendar calendar, int dias) {
        if (dias == 0) return calendar;
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar;
    }
}
