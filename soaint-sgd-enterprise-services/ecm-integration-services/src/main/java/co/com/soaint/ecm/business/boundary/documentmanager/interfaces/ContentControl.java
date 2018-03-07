package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import org.apache.chemistry.opencmis.client.api.Session;
import org.springframework.stereotype.Service;

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
    MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder);

    /**
     * Subir documento Principal Adjuntos al ECM
     *
     * @param session      Objeto conexion
     * @param documentoDTO Objeto qeu contiene los metadatos de los documentos ECM
     * @param selector     Selector que dice donde se va a gauardar el documento
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documentoDTO, String selector) throws IOException;

    /**
     * Subir Versionar documento Generado al ECM
     *
     * @param session   Objeto conexion
     * @param documento documento a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirVersionarDocumentoGenerado(Session session, DocumentoDTO documento, String selector) throws IOException;

    /**
     * Obtener documento Adjunto dado id Documento Principal
     *
     * @param session   Objeto conexion
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerDocumentosAdjuntos(Session session, DocumentoDTO documento) throws IOException;

    /**
     * Obtener versiones del documento dado id Documento
     *
     * @param session Objeto conexion
     * @param idDoc   documento a subir
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerVersionesDocumento(Session session, String idDoc) throws IOException;

    /**
     * Modificar documento Content
     *
     * @param session             Objeto conexion
     * @param idDocumento         nombre de documento
     * @param nroRadicado         número de radicado del documento
     * @param tipologiaDocumental tipologia documental
     * @param nombreRemitente     Nombre del remitente
     * @return MensajeRespuesta
     * @throws IOException exception
     */
    MensajeRespuesta modificarMetadatosDocumento(Session session, String idDocumento, String nroRadicado, String tipologiaDocumental, String nombreRemitente) throws IOException;


    /**
     * Descargar documento
     *
     * @param documentoDTO Objeto que contiene metadatos del documento en el ECM
     * @param session      Objeto conexion
     * @return Se retorna el documento
     */
    MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO, Session session) throws IOException;

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

    /**
     * Servicio que devuelve el listado de las Series y de las Dependencias del ECM
     *
     * @param dependenciaTrdDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session           Objeto de conexion
     * @return Objeto de dependencia que contiene las sedes o las dependencias buscadas
     */
    MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session);

    /**
     * Servicio que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session           Objeto de conexion
     * @return MensajeRespuesta
     */
    MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session);

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    MensajeRespuesta listarUnidadesDocumentales(Session session);
}