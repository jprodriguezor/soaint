package co.com.soaint.ecm.business.boundary.mediator.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.util.List;

/**
 * Created by dasiel on 01/06/2017.
 */
public interface EcmManagerMediator {
     MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) throws SystemException;

     String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws SystemException;

     MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws SystemException;
}
