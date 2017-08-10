/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.util.List;

/**
 * @author sarias
 */
public abstract class ContentManagerMediator {
    /**
     * Metodo de la clase
     */
    public ContentManagerMediator() {

    }

    /**
     * Crear Estructura
     * @param structure Lista qeu contiene la estructura
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws InfrastructureException Excepcion lanzada en error
     */
    public abstract MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) throws InfrastructureException;

    /**
     * Subir documento
     * @param nombreDocumento Nombre de documento
     * @param documento Documento
     * @param tipoComunicacion Tipo de comunicacion
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion lanzada en error
     */
    public abstract String subirDocumentoContent(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException;

    /**
     *
     * @param documento Nombre de documento
     * @param carpetaFuente Carpeta donde esta el documento
     * @param carpetaDestino Carpeta a donde se va a mover
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws InfrastructureException Excepcion lanzada en error
     */
    public abstract MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) throws InfrastructureException;
}
