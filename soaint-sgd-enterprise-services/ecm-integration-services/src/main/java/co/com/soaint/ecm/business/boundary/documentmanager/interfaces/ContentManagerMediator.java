/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;

import java.util.List;

/**
 *
 * @author sarias
 */
public abstract class ContentManagerMediator {

    public ContentManagerMediator(){

    }
    public abstract MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) throws InfrastructureException;
    public abstract String subirDocumentoContent(String tipoComunicacion,String caminoLocal, String nombreDocumento,String user,String titulo,String descripcionString) throws InfrastructureException;

    public abstract MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException;
}
