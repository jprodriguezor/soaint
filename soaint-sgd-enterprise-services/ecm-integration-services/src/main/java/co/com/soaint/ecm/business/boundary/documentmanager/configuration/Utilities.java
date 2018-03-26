/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;

import java.util.*;

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
     * Metodo suma cierta cantidad de dias a una fecha dada
     *
     * @param fecha fecha inicial
     * @param campo tipo por el que se hacer la variacion(Calendar.HOUR, DAY_OF_YEAR, etc)
     * @param valor valor a sumar o restar
     * @return Date fecha final
     */
    public static Date variarFecha(Date fecha, int campo, int valor) {
        if (valor == 0) return fecha;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(campo, valor);
        return calendar.getTime();
    }

    /**
     * Metodo suma cierta cantidad de dias a una fecha dada
     *
     * @param date fecha inicial
     * @param dias     cantidad de dias a sumar
     * @return Date fecha final
     */
    public static Date sumarDiasAFecha(Date date, int dias) {
        return variarFecha(date, Calendar.DAY_OF_YEAR, dias);
    }
}
