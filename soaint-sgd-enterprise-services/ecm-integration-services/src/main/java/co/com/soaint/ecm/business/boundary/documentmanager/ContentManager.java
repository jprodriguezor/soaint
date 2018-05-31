package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;


/**
 * Created by dasiel
 */

@Log4j2
@BusinessBoundary
public class ContentManager implements Serializable {
    
    private static final Long serialVersionUID = 150L;
    
    private static final String MSGCONEXION = "### Estableciendo la conexion..";

    @Autowired
    private ContentControl contentControl;
    private Conexion conexion;

    @PostConstruct
    public void init() {
        log.info(MSGCONEXION);
        this.conexion = contentControl.obtenerConexion();
    }

    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) {
        log.info("### Creando estructura content..");
        final Utilities utils = new Utilities();
        for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
            utils.ordenarListaOrganigrama(EstructuraTrdDTO.getOrganigramaItemList());
        }
        log.info("### Estableciendo Conexion con el ECM..");
        Carpeta carpeta = new Carpeta();
        carpeta.setFolder(conexion.getSession().getRootFolder());
        return contentControl.generarArbol(structure, carpeta);        
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoContent(DocumentoDTO documento, String selector) throws SystemException {
        log.info("### Subiendo documento principal/adjunto al content..");
        log.info("### Se invoca el metodo de subir el documento principal/adjunto..");
        return contentControl.subirDocumentoPrincipalAdjunto(conexion.getSession(), documento, selector);
    }

    /**
     * Metodo generico para crear el link del documento en el content
     *
     * @param documento Documento que se va a subir
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta crearLinkContent(DocumentoDTO documento) {
        log.info("### Creando link del documento en el content..");
        log.info("### Se invoca el metodo crearLinkDocumentosApoyo..");
        return contentControl.crearLinkDocumentosApoyo(conexion.getSession(), documento);
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirVersionarDocumentoGeneradoContent(DocumentoDTO documento, String selector) {
        log.info("### Subiendo versionando documento generado al content..");
        log.info("### Se invoca el metodo de subir/versionar el documento..");
        return contentControl.subirVersionarDocumentoGenerado(conexion.getSession(), documento, selector);
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerDocumentosAdjuntosContent(DocumentoDTO documento) {
        log.info("### Obtener documento principal y adjunto del content..");
        log.info("### Se invoca el metodo de obtener documentos principales y adjuntos..");
        return contentControl.obtenerDocumentosAdjuntos(conexion.getSession(), documento);
    }

    /**
     * Metodo generico para obtener las versiones de un documento del content
     *
     * @param idDoc Id Documento que se va  a pedir Versiones
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerVersionesDocumentoContent(String idDoc) {
        log.info("### Obtener versiones documento del content..");
        log.info("### Se invoca el metodo de obtener versiones del documento..");
        return contentControl.obtenerVersionesDocumento(conexion.getSession(), idDoc);
    }

    /**
     * Metodo generico para subir los dccumentos al content
     *
     * @param metadatosDocumentos Metadatos del documento a modificar
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta modificarMetadatoDocumentoContent(DocumentoDTO metadatosDocumentos) {
        log.info("### Modificando metadatos del documento..");log.info("### Se invoca el metodo de modificar el documento..");
        return contentControl.modificarMetadatosDocumento(conexion.getSession(),
                metadatosDocumentos.getIdDocumento(), metadatosDocumentos.getNroRadicado(),
                metadatosDocumentos.getTipologiaDocumental(), metadatosDocumentos.getNombreRemitente());
    }

    /**
     * Metodo generico para mover documentos dentro del ECM
     *
     * @param documento      Identificador del documento que se va a mover
     * @param carpetaFuente  Carpeta donde esta actualmente el documento.
     * @param carpetaDestino Carpeta a donde se movera el documento.
     * @return Mensaje de respuesta del metodo (coigo y mensaje)
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) {
        log.info("### Moviendo Documento " + documento + " desde la carpeta: " + carpetaFuente + " a la carpeta: " + carpetaDestino);
        return contentControl.movDocumento(conexion.getSession(), documento, carpetaFuente, carpetaDestino);
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param documentoDTO Metadatos del documento dentro del ECM
     * @return Documento
     */
    public MensajeRespuesta descargarDocumentoContent(DocumentoDTO documentoDTO) throws SystemException {
        log.info("### Descargando documento del content..");
        log.info("### Se invoca el metodo de descargar el documento..");
        return contentControl.descargarDocumento(documentoDTO, conexion.getSession());
    }

    /**
     * Metodo generico para eliminar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return true en exito y false en error
     */
    public void eliminarDocumento(String idDoc) throws SystemException {
        log.info("### Eliminando documento del content..");
        log.info("### Se invoca el metodo de eliminar el documento..");
        contentControl.eliminardocumento(idDoc, conexion.getSession());
    }

    /**
     * Metodo generico para devolver series o subseries
     *
     * @param contenidoDependenciaTrdDTO Objeto que contiene los datos para realizar la busqueda
     * @return Objeto response
     */
    public MensajeRespuesta devolverSeriesSubseries(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) throws SystemException {
        log.info("### Obteniendo las series y subseries del content..");
        log.info("### Se invoca el metodo de devolver serie o subserie..");
        MensajeRespuesta response = contentControl.devolverSerieSubSerie(contenidoDependenciaTrdDTO, conexion.getSession());
        log.info("Series o subseries devueltas exitosamente");
        return response;
    }

    /**
     * Metodo para devolver crear las unidades documentales
     *
     * @param unidadDocumentalDTO DTO que contiene los parametro de búsqueda
     * @return MensajeRespuesta
     */
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Creando la unidad documental {} ..", unidadDocumentalDTO);
        log.info("### Invocando metodo para crear Unidad Documental..");
        return contentControl.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Listando las Unidades Documentales listarUnidadDocumental method");
        return contentControl.listarUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws Exception {
        log.info("### mostrando la UnidadDocumental obtenerDetallesDocumentoDTO(String idDocumento) method");
        return contentControl.obtenerDetallesDocumentoDTO(idDocumento, conexion.getSession());
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws Exception {
        log.info("### Ejecutando MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental)");
        return contentControl.detallesUnidadDocumental(idUnidadDocumental, conexion.getSession());
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Ejecutando MensajeRespuesta subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        return contentControl.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, conexion.getSession());
    }

    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener los documentos por archivar en el ECM");
        return contentControl.getDocumentosPorArchivar(codigoDependencia, conexion.getSession());
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("processing rest request - modificar las unidades documentales en el ECM");
        return contentControl.modificarUnidadesDocumentales(unidadDocumentalDTOS, conexion.getSession());
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) throws SystemException {
        log.info("processing rest request - Subir Documentos temporales ECM");
        return contentControl.subirDocumentosTemporalesUD(documentoDTOS, conexion.getSession());
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    public MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener Documentos archivados ECM");
        return contentControl.obtenerDocumentosArchivados(codigoDependencia, conexion.getSession());
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    public MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException {
        log.info("processing rest request - Obtener las sedes, dependencias, series o subseries");
        return contentControl.listarDependenciaMultiple(dependenciaTrdDTO, conexion.getSession());
    }

    /**
     * Crear carpeta en el Record
     *
     * @param disposicionFinalDTO Obj con el DTO Unidad Documental y l listado de las disposiciones
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO) throws SystemException {
        log.info("processing rest request - Listar las unidades documentales que hay culminado su tiempo de retencion");
        return contentControl.listarUdDisposicionFinal(disposicionFinalDTO, conexion.getSession());
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        return contentControl.aprobarRechazarDisposicionesFinales(unidadDocumentalDTOS, conexion.getSession());
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Subir Documento temporal ECM");
        return contentControl.subirDocumentoTemporalUD(documentoDTO, conexion.getSession());
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param documentoDTO Obj con el tag a agregar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta estamparEtiquetaRadicacion(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Estampar la etiquta de radicacion");
        return contentControl.estamparEtiquetaRadicacion(documentoDTO, conexion.getSession());
    }

    /**
     * Subir Documento Anexo al ECM
     *
     * @param documento DTO que contiene los datos del documento Anexo
     * @return MensajeRespuesta DocumentoDTO adicionado
     */
    public MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento) throws SystemException {
        log.info("processing rest request - Subir Documento Anexo al ECM:");
        return contentControl.subirDocumentoAnexo(documento, conexion.getSession());
    }
}
