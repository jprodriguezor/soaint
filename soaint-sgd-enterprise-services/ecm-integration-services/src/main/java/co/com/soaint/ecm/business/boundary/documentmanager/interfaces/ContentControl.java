package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
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

    // Class Properties ECM
    String CLASE_BASE = "claseBase";
    String CLASE_DEPENDENCIA = "claseDependencia";
    String CLASE_SERIE = "claseSerie";
    String CLASE_SUBSERIE = "claseSubserie";
    String CLASE_UNIDAD_DOCUMENTAL = "claseUnidadDocumental";
    String CMCOR = "cmcor:";

    //******************
    //* properties ECM *
    //******************

    // CM_Unidad_Administrativa
    String CMCOR_DEP_CODIGO = CMCOR + "CodigoDependencia";
    String CMCOR_DEP_CODIGO_UAP = CMCOR + "CodigoUnidadAdminPadre";

    //CM_Unidad_Documental
    String CMCOR_UD_ACCION = CMCOR + "accion";
    String CMCOR_UD_FECHA_INICIAL = CMCOR + "fechaInicial";
    String CMCOR_UD_INACTIVO = CMCOR + "inactivo";
    String CMCOR_UD_UBICACION_TOPOGRAFICA = CMCOR + "ubicacionTopografica";
    String CMCOR_UD_FECHA_FINAL = CMCOR + "fechaFinal";
    String CMCOR_UD_FECHA_CIERRE = CMCOR + "fechaCierre";
    String CMCOR_UD_ID = CMCOR + "id";
    String CMCOR_UD_FASE_ARCHIVO = CMCOR + "faseArchivo";
    String CMCOR_UD_SOPORTE = CMCOR + "soporte";
    String CMCOR_UD_CODIGO = CMCOR + "codigoUnidadDocumental";
    String CMCOR_UD_DESCRIPTOR_2 = CMCOR + "descriptor2";
    String CMCOR_UD_DESCRIPTOR_1 = CMCOR + "descriptor1";
    String CMCOR_UD_CERRADA = CMCOR + "cerrada";
    String CMCOR_UD_OBSERVACIONES = CMCOR + "observaciones";
    String CMCOR_UD_DISPOSICION = CMCOR + "disposicion";
    String CMCOR_UD_ESTADO = CMCOR + "estado";

    //CM_Unidad_Base
    String CMCOR_UB_CODIGO = CMCOR + "CodigoBase";

    //CM_Serie
    String CMCOR_SER_CODIGO = CMCOR + "CodigoSerie";

    //CM_SubSerie
    String CMCOR_SS_CODIGO = CMCOR + "CodigoSubserie";

    //CM_DocumentoPersonalizado
    String CMCOR_NRO_RADICADO = CMCOR + "NroRadicado";
    String CMCOR_NOMBRE_REMITENTE = CMCOR + "NombreRemitente";
    String CMCOR_TIPOLOGIA_DOCUMENTAL = CMCOR + "TipologiaDocumental";
    String CMCOR_TIPO_DOCUMENTO = CMCOR + "xTipo";
    String CMCOR_NUMERO_REFERIDO = CMCOR + "xNumeroReferido";
    String CMCOR_ID_DOC_PRINCIPAL = CMCOR + "xIdentificadorDocPrincipal";

    // ECM sms Error
    String ECM_ERROR = "ECM_ERROR";
    String ECM_ERROR_DUPLICADO = "ECM ERROR DUPLICADO";
    String EXISTE_CARPETA = "Existe la Carpeta: ";

    // ECM sms
    String COMUNICACIONES_INTERNAS_RECIBIDAS = "Comunicaciones Oficiales Internas Recibidas ";
    String COMUNICACIONES_INTERNAS_ENVIADAS = "Comunicaciones Oficiales Internas Enviadas ";
    String COMUNICACIONES_EXTERNAS_RECIBIDAS = "Comunicaciones Oficiales Externas Recibidas ";
    String COMUNICACIONES_EXTERNAS_ENVIADAS = "Comunicaciones Oficiales Externas Enviadas ";
    String TIPO_COMUNICACION_INTERNA = "0231.02311_Comunicaciones Oficiales Internas";
    String TIPO_COMUNICACION_EXTERNA = "0231.02312_Comunicaciones Oficiales Externas";
    String ERROR_TIPO_EXCEPTION = "### Error tipo Exception----------------------------- :";
    String ERROR_TIPO_IO = "### Error tipo IO----------------------------- :";
    String CONTENT_DISPOSITION = "Content-Disposition";
    String DOCUMENTO = "documento";
    String APPLICATION_PDF = "application/pdf";
    String PRODUCCION_DOCUMENTAL = "PRODUCCION DOCUMENTAL ";
    String DOCUMENTOS_APOYO = "DOCUMENTOS DE APOYO ";
    String AVISO_CREA_DOC = "### Se va a crear el documento..";
    String AVISO_CREA_DOC_ID = "### Documento creado con id ";
    String NO_EXISTE_DEPENDENCIA = "En la estructura no existe la Dependencia: ";
    String NO_EXISTE_SEDE = "En la estructura no existe la sede: ";
    String SEPARADOR = "---";
    String F_CMCOR = "F:cmcor:";

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
    MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws BusinessException, Exception;

    /**
     * Servicio que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session             Objeto de conexion
     * @return MensajeRespuesta
     */
    MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws BusinessException;

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws BusinessException;

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @param session     Objeto conexion de Alfresco
     * @return MensajeRespuesta con los detalles del documento
     */
    MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session) throws Exception;

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idUnidadDocumental La unidad documental
     * @param session            Objeto conexion de Alfresco
     */
    MensajeRespuesta listaDocumentosDTOUnidadDocumental(String idUnidadDocumental, Session session) throws Exception;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental, Session session) throws BusinessException;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental, Session session);

    /**
     * Metodo que devuelve una Unidad Documental con sus Documentos
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return UnidadDocumentalDTO      Unidad Documental
     */
    UnidadDocumentalDTO listarDocsDadoIdUD(String idUnidadDocumental, Session session) throws Exception;

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
    boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws Exception;

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    List<DocumentoDTO> getDocumentsFromFolder(Folder folder) throws Exception;

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @param documentoDTOS       Lista de documentos a guardar
     * @param session             Obj conexion de alfresco
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, List<DocumentoDTO> documentoDTOS, Session session);

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @param documentoDTO        Documento a guardar
     * @param session             Obj conexion de alfresco
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta subirDocumentoUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO, DocumentoDTO documentoDTO, Session session);

    void subirDocumentosCMISPrincipalAnexoUD(Folder folder, List<Document> documentos) throws BusinessException;

    Map<String, Object> obtenerPropiedadesDocumento(Document document);

    /**
     * Operacion para devolver los documentos por archivar
     */
    MensajeRespuesta getDocumentosPorArchivar(Session session) throws Exception;

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS    Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) throws Exception;
}