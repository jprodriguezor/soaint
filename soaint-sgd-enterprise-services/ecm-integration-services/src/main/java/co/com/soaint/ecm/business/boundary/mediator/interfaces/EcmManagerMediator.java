package co.com.soaint.ecm.business.boundary.mediator.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.util.List;

public interface EcmManagerMediator {
     /**
      * Interfaz para crear la estructura dentro del ECM
      * @param structure Objeto que contiene la estrucutra a crear en el ECM
      * @return Mensaje de respuesta del metodo(codigo y mensaje)
      */
     MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure);

     /**
      * Interfaz para subir documento en el ECM
      * @param nombreDocumento Nombre del documento a subir
      * @param documento Documento que se va a subir
      * @param tipoComunicacion TIpo de comunicacion que puede ser Externa o Interna
      * @return Mensaje de respuesta del metodo(codigo y mensaje)
      */
     String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion);

     /**
      * Interfaz para mover documento en el ECM
      * @param documento Nombre del documento a mover
      * @param CarpetaFuente Carpeta donde se encuentra el documento
      * @param CarpetaDestino Carpeta a donde se va a mover el documento.
      * @return Mensaje de respuesta del metodo(codigo y mensaje)
      */
     MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino);
}
