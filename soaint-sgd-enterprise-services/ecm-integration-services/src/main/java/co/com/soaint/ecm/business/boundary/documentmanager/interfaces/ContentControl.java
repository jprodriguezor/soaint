package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.MetadatosDocumentosDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import org.apache.chemistry.opencmis.client.api.Session;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Creado por Dasiel
 */
@Service
public interface ContentControl {

    /**
     * Obtener objeto conexion
     *
     * @return Objeto conexion
     */
    Conexion obtenerConexion();

    /**
     * Generar estructura
     *
     * @param estructuraList lista de estructura
     * @param folder         carpeta padre
     * @return mensaje respuesta
     */
    MensajeRespuesta generarArbol(List <EstructuraTrdDTO> estructuraList, Carpeta folder);

    /**
     * Subir documento
     *
     * @param session          Objeto conexion
     * @param nombreDocumento  nombre de documento
     * @param documento        documento a subir
     * @param tipoComunicacion tipo de comunicacion
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException;

    /**
     * Subir documento Principal Adjuntos al ECM
     *
     * @param session          Objeto conexion
     * @param documento        documento a subir
     * @param metadatosDocumentosDTO Objeto qeu contiene los metadatos de los documentos ECM
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, MultipartFormDataInput documento, MetadatosDocumentosDTO metadatosDocumentosDTO) throws IOException;

    /**
     * Subir Versionar documento Generado al ECM
     *
     * @param session          Objeto conexion
     * @param documento        documento a subir/versionar
     * @param metadatosDocumentosDTO Objeto qeu contiene los metadatos de los documentos ECM
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirVersionarDocumentoGenerado(Session session, MultipartFormDataInput documento, MetadatosDocumentosDTO metadatosDocumentosDTO) throws IOException;

    /**
     * Obtener documento Adjunto dado id Documento Principal
     *
     * @param session          Objeto conexion
     * @param idDocPrincipal        documento a subir
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerDocumentosAdjuntos(Session session,String idDocPrincipal) throws IOException;

    /**
     * Obtener versiones del documento dado id Documento
     *
     * @param session          Objeto conexion
     * @param idDoc        documento a subir
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerVersionesDocumento(Session session,String idDoc) throws IOException;

    /**
     * Modificar documento Content
     *
     * @param session          Objeto conexion
     * @param idDocumento  nombre de documento
     * @param nroRadicado        número de radicado del documento
     * @param tipologiaDocumental tipologia documental
     * @param nombreRemitente Nombre del remitente
     * @return MensajeRespuesta
     * @throws IOException exception
     */
    MensajeRespuesta modificarMetadatosDocumento(Session session, String idDocumento, String nroRadicado, String tipologiaDocumental,String nombreRemitente) throws IOException;



    /**
     * Descargar documento
     *
     * @param metadatosDocumentosDTO Objeto que contiene metadatos del documento en el ECM
     * @param session     Objeto conexion
     * @return Se retorna el documento
     */
    Response descargarDocumento(MetadatosDocumentosDTO metadatosDocumentosDTO, Session session) throws IOException;

    /**
     * MOver documento
     *
     * @param session        objeto conexion
     * @param documento      nombre de documento
     * @param carpetaFuente  carpeta fuente
     * @param carpetaDestino carpeta destino
     * @return mensaje respuesta
     */
    MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino);

    /**
     * Eliminar documento del ECM
     *
     * @param idDoc   Identificador del documento a borrar
     * @param session Objeto de conexion al Alfresco
     * @return Retorna true si borró con exito y false si no
     */
    boolean eliminardocumento(String idDoc, Session session);


}