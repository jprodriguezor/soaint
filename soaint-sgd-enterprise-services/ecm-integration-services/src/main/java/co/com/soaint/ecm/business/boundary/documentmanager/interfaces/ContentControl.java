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

/**
 * Creado por Dasiel
 */
@Service
public interface ContentControl {

    /**
     *Obtener objeto conexion
     * @return Objeto conexion
     */
    Conexion obtenerConexion();

    /**
     *MOver documento
     * @param session objeto conexion
     * @param documento nombre de documento
     * @param carpetaFuente carpeta fuente
     * @param carpetaDestino carpeta destino
     * @return mensaje respuesta
     */
    MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino);

    /**
     *Generar estructura
     * @param estructuraList lista de estructura
     * @param folder carpeta padre
     * @return mensaje respuesta
     */
    MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder);

    /**
     *Subir documento
     * @param session lista de estructura
     * @param nombreDocumento nombre de documento
     * @param documento documento a subir
     * @param tipoComunicacion tipo de comunicacion
     * @return ide de documento
     * @throws IOException exception
     */
    String subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException;


}
