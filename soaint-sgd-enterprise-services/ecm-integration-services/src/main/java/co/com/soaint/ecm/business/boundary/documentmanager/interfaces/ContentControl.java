package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import org.apache.chemistry.opencmis.client.api.Session;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public interface ContentControl {


    Conexion obtenerConexion();

    MensajeRespuesta movDocumento(Session session, String documento, String CarpetaFuente, String carpetaDestino);

    MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder);

    String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException;


}
