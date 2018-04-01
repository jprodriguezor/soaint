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

    //cmcor: CM_Unidad_Administrativa
    String CMCOR_DEP_CODIGO = "cmcor:CodigoDependencia";
    String CMCOR_DEP_CODIGO_UAP = "cmcor:CodigoUnidadAdminPadre";

    //cmcor:CM_Unidad_Documental
    String CMCOR_UD_ACCION = "cmcor:accion";
    String CMCOR_UD_FECHA_INICIAL = "cmcor:fechaInicial";
    String CMCOR_UD_INACTIVO = "cmcor:inactivo";
    String CMCOR_UD_UBICACION_TOPOGRAFICA = "cmcor:ubicacionTopografica";
    String CMCOR_UD_FECHA_FINAL = "cmcor:fechaFinal";
    String CMCOR_UD_FECHA_CIERRE = "cmcor:fechaCierre";
    String CMCOR_UD_ID = "cmcor:id";
    String CMCOR_UD_FASE_ARCHIVO = "cmcor:faseArchivo";
    String CMCOR_UD_SOPORTE = "cmcor:soporte";
    String CMCOR_UD_CODIGO = "cmcor:codigoUnidadDocumental";
    String CMCOR_UD_DESCRIPTOR_2 = "cmcor:descriptor2";
    String CMCOR_UD_DESCRIPTOR_1 = "cmcor:descriptor1";
    String CMCOR_UD_CERRADA = "cmcor:cerrada";
    String CMCOR_UD_OBSERVACIONES = "cmcor:observaciones";

    //cmcor:CM_Unidad_Base
    String CMCOR_UB_CODIGO = "cmcor:CodigoBase";

    //cmcor:CM_Serie
    String CMCOR_SER_CODIGO = "cmcor:CodigoSerie";

    //cmcor:CM_SubSerie
    String CMCOR_SS_CODIGO = "cmcor:CodigoSubserie";

    //CM_DocumentoPersonalizado
    String CMCOR_NRO_RADICADO = "cmcor:NroRadicado";
    String CMCOR_NOMBRE_REMITENTE = "cmcor:NombreRemitente";
    String CMCOR_TIPOLOGIA_DOCUMENTAL = "cmcor:TipologiaDocumental";
    String CMCOR_TIPO_DOCUMENTO = "cmcor:xTipo";
    String CMCOR_NUMERO_REFERIDO = "cmcor:xNumeroReferido";
    String CMCOR_ID_DOC_PRINCIPAL = "cmcor:xIdentificadorDocPrincipal";

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
     * @param session                Objeto conexion
     * @param documentoDTO Objeto qeu contiene los metadatos de los documentos ECM
     * @param selector               Selector que dice donde se va a gauardar el documento
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documentoDTO, String selector) throws IOException;

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     * @param session Objeto de conexion a Alfresco
     * @param documento Objeto que contiene los datos del documento
     * @return
     */
    MensajeRespuesta crearLinkDocumentosApoyo(Session session, DocumentoDTO documento);

    /**
     * Subir Versionar documento Generado al ECM
     *
     * @param session                Objeto conexion
     * @param documento              documento a subir/versionar
     * @param selector               parametro que indica donde se va a guardar el documento
     * @return ide de documento
     * @throws IOException exception
     */
    MensajeRespuesta subirVersionarDocumentoGenerado(Session session,  DocumentoDTO documento, String selector) throws IOException;

    /**
     * Obtener documento Adjunto dado id Documento Principal
     *
     * @param session        Objeto conexion
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
     * @param session                Objeto conexion
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
     MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) ;

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
    MensajeRespuesta listarUnidadesDocumentales(UnidadDocumentalDTO unidadDocumentalDTO, Session session);

}