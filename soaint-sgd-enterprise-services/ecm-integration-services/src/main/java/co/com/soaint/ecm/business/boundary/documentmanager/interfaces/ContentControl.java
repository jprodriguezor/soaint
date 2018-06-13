package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Creado por Dasiel
 */
@Service
public interface ContentControl extends Serializable {

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
    MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documentoDTO, String selector, boolean requiereEtiqueta) throws SystemException;

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento Objeto que contiene los datos del documento
     * @return
     */
    MensajeRespuesta crearLinkDocumentosApoyo(Session session, DocumentoDTO documento);

    /**
     * Subir Versionar documento Generado al ECM
     *
     * @param session   Objeto conexion
     * @param documento documento a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirVersionarDocumentoGenerado(Session session, DocumentoDTO documento, String selector);

    /**
     * Obtener documento Adjunto dado id Documento Principal
     *
     * @param session   Objeto conexion
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerDocumentosAdjuntos(Session session, DocumentoDTO documento);

    /**
     * Obtener versiones del documento dado id Documento
     *
     * @param session Objeto conexion
     * @param idDoc   documento a subir
     * @return Lista de documentos adjuntos
     * @throws IOException exception
     */
    MensajeRespuesta obtenerVersionesDocumento(Session session, String idDoc);

    /**
     * Metodo para modificar metadatos del documento de Alfresco
     *
     * @param session             Objeto de conexion a Alfresco
     * @param dto                 Obj DocumentoDTO con las modificaciones
     */
    MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO dto, Session session) throws SystemException;


    /**
     * Descargar documento
     *
     * @param documentoDTO Objeto que contiene metadatos del documento en el ECM
     * @param session      Objeto conexion
     * @return Se retorna el documento
     */
    MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO, Session session) throws SystemException;

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
    void eliminardocumento(String idDoc, Session session) throws SystemException;

    /**
     * Servicio que devuelve el listado de las Series y de las Dependencias del ECM
     *
     * @param dependenciaTrdDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session           Objeto de conexion
     * @return Objeto de dependencia que contiene las sedes o las dependencias buscadas
     */
    MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws SystemException;

    /**
     * Servicio que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session             Objeto de conexion
     * @return MensajeRespuesta
     */
    MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException;

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException;

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @param session     Objeto conexion de Alfresco
     * @return MensajeRespuesta con los detalles del documento
     */
    MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session) throws SystemException;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental, Session session) throws SystemException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @param fullDocuments  true para devolver todos los documentos, false de otro modo
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental, boolean fullDocuments, Session session);

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental, Session session);

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idProperty Id de la Unidad Documental
     * @param session    Objeto conexion de Alfresco
     * @return Folder si existe, null si no existe
     */
    Optional<Folder> getUDFolderById(String idProperty, Session session);

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @param session             Objeto conexion de Alfresco
     * @return boolean true/false
     */
    boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    List<DocumentoDTO> getDocumentsFromFolder(Folder folder) throws SystemException;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException;

    Map<String, Object> obtenerPropiedadesDocumento(Document document);

    /**
     * Operacion para devolver los documentos por archivar
     */
    MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia, Session session) throws SystemException;

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS    Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) throws SystemException;

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS, Session session) throws SystemException;

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO, Session session) throws SystemException;

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia, Session session) throws SystemException;

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws SystemException;

    /**
     * Crear carpeta en el Record
     *
     * @param disposicionFinalDTO Obj con el DTO Unidad Documental y l listado de las disposiciones
     * @return Mensaje de respuesta
     */
    MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO, Session session) throws SystemException;

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) throws SystemException;

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param documentoDTO Obj con el tag a agregar
     * @return MensajeRespuesta
     */
    MensajeRespuesta estamparEtiquetaRadicacion(DocumentoDTO documentoDTO, Session session) throws SystemException;

    /**
     * Subir Documento Anexo al ECM
     *
     * @param documento DTO que contiene los datos del documento Anexo
     * @return MensajeRespuesta DocumentoDTO adicionado
     */
    MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento, Session session) throws SystemException;
}