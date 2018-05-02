package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


/**
 * Created by dasiel
 */

@BusinessBoundary
@NoArgsConstructor
public class ContentManager {
    @Autowired
    private
    ContentControl contentControl;
    private static final String MSGCONEXION = "### Estableciendo la conexion..";
    private static final Logger logger = LogManager.getLogger(ContentManager.class.getName());


    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) {
        logger.info("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta();
        Carpeta carpeta;
        try {

            Utilities utils = new Utilities();
            new Conexion();
            Conexion conexion;
            for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
                utils.ordenarListaOrganigrama(EstructuraTrdDTO.getOrganigramaItemList());
            }

            logger.info("### Estableciendo Conexion con el ECM..");
            conexion = contentControl.obtenerConexion();

            carpeta = new Carpeta();
            carpeta.setFolder(conexion.getSession().getRootFolder());
            response = contentControl.generarArbol(structure, carpeta);

        } catch (Exception e) {
            response.setCodMensaje("Error creando estructura");
            response.setMensaje("11113");
            logger.error("Error creando estructura", e);
        }
        return response;
    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoContent(DocumentoDTO documento, String selector) throws IOException {

        logger.info("### Subiendo documento principal/adjunto al content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de subir el documento principal/adjunto..");
            response = contentControl.subirDocumentoPrincipalAdjunto(conexion.getSession(), documento, selector);

        } catch (Exception e) {
            logger.error("Error subiendo documento principal/adjunto", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error al crear el documento principal/adjunto");
            throw e;
        }
        return response;

    }
    /**
     * Metodo generico para crear el link del documento en el content
     *
     * @param documento Documento que se va a subir
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta crearLinkContent(DocumentoDTO documento) throws IOException {

        logger.info("### Creando link del documento en el content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo crearLinkDocumentosApoyo..");
            response = contentControl.crearLinkDocumentosApoyo(conexion.getSession(), documento);

        } catch (Exception e) {
            logger.error("Error Creando link del documento en el content", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error Creando link del documento en el content");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento Documento que se va a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirVersionarDocumentoGeneradoContent(DocumentoDTO documento, String selector) throws IOException {

        logger.info("### Subiendo versionando documento generado al content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de subir/versionar el documento..");
            response = contentControl.subirVersionarDocumentoGenerado(conexion.getSession(), documento, selector);

        } catch (Exception e) {
            logger.error("Error subiendo/versionando documento", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error al crear/versionar el documento");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para subir los dccumentos adjuntos al content
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerDocumentosAdjuntosContent(DocumentoDTO documento) throws IOException {

        logger.info("### Obtener documento principal y adjunto del content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de obtener documentos principales y adjuntos..");
            response = contentControl.obtenerDocumentosAdjuntos(conexion.getSession(), documento);

        } catch (Exception e) {
            logger.error("Error obteniendo documento adjunto y principal", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error obteniendo documento adjunto y principal");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para obtener las versiones de un documento del content
     *
     * @param idDoc Id Documento que se va  a pedir Versiones
     * @return Lista de objetos de documentos asociados al idDocPrincipal
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta obtenerVersionesDocumentoContent(String idDoc) throws IOException {

        logger.info("### Obtener versiones documento del content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de obtener versiones del documento..");
            response = contentControl.obtenerVersionesDocumento(conexion.getSession(), idDoc);

        } catch (Exception e) {
            logger.error("Error obteniendo versiones del documento", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error obteniendo versiones del documento");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para subir los dccumentos al content
     *
     * @param metadatosDocumentos Metadatos del documento a modificar
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta modificarMetadatoDocumentoContent(DocumentoDTO metadatosDocumentos) throws IOException {

        logger.info("### Modificando metadatos del documento..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();

            logger.info("### Se invoca el metodo de modificar el documento..");

            response = contentControl.modificarMetadatosDocumento(conexion.getSession(), metadatosDocumentos.getIdDocumento(), metadatosDocumentos.getNroRadicado(), metadatosDocumentos.getTipologiaDocumental(), metadatosDocumentos.getNombreRemitente());

        } catch (Exception e) {
            logger.error("Error modificando el documento", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error al modificar el documento");
            throw e;
        }
        return response;

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

        logger.info("### Moviendo Documento " + documento + " desde la carpeta: " + carpetaFuente + " a la carpeta: " + carpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta();

        try {

            logger.info("###Se va a establecer la conexion");
            Conexion conexion;
            new Conexion();
            conexion = contentControl.obtenerConexion();
            logger.info("###Conexion establecida");
            response = contentControl.movDocumento(conexion.getSession(), documento, carpetaFuente, carpetaDestino);

        } catch (Exception e) {
            logger.error("Error moviendo documento", e);
            response.setCodMensaje("0003");
            response.setMensaje("Error moviendo documento, esto puede ocurrir al no existir alguna de las carpetas");
        }
        return response;
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param documentoDTO Metadatos del documento dentro del ECM
     * @return Documento
     */
    public MensajeRespuesta descargarDocumentoContent(DocumentoDTO documentoDTO) {

        logger.info("### Descargando documento del content..");
        MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de descargar el documento..");
            respuesta = contentControl.descargarDocumento(documentoDTO, conexion.getSession());

        } catch (Exception e) {
            logger.error("Error descargando documento", e);

        }
        logger.info("### Se devuelve el documento del content..");
        return respuesta;

    }

    /**
     * Metodo generico para eliminar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return true en exito y false en error
     */
    public boolean eliminarDocumento(String idDoc) {

        logger.info("### Eliminando documento del content..");

        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de eliminar el documento..");
            if (contentControl.eliminardocumento(idDoc, conexion.getSession())) {
                logger.info("Documento eliminado con exito");
                return Boolean.TRUE;
            } else
                return Boolean.FALSE;

        } catch (Exception e) {
            logger.error("Error eliminando documento", e);
            return Boolean.FALSE;
        }
    }

    /**
     * Metodo generico para devolver series o subseries
     *
     * @param contenidoDependenciaTrdDTO Objeto que contiene los datos para realizar la busqueda
     * @return Objeto response
     */
    public MensajeRespuesta devolverSeriesSubseries(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) {
        MensajeRespuesta response = new MensajeRespuesta();
        logger.info("### Obteniendo las series y subseries del content..");

        try {
            Conexion conexion;
            new Conexion();
            logger.info(MSGCONEXION);
            conexion = contentControl.obtenerConexion();
            logger.info("### Se invoca el metodo de devolver serie o subserie..");
            response = contentControl.devolverSerieSubSerie(contenidoDependenciaTrdDTO, conexion.getSession());
            logger.info("Series o subseries devueltas exitosamente");

        } catch (Exception e) {
            logger.error("Error obteniendo las series o subseries", e);
            response.setCodMensaje("2222");
            response.setMensaje("Error obteniendo las series o subseries");

        }
        return response;
    }

    /**
     * Metodo para devolver crear las unidades documentales
     * @param unidadDocumentalDTO DTO que contiene los parametro de búsqueda
     * @return MensajeRespuesta
     */
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws BusinessException {
        logger.info("### Creando la unidad documental {} ..", unidadDocumentalDTO);
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        logger.info("### Invocando metodo para crear Unidad Documental..");
        return contentControl.crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws BusinessException {
        logger.info("### Listando las Unidades Documentales listarUnidadDocumental method");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.listarUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento     Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws BusinessException {
        logger.info("### mostrando la UnidadDocumental obtenerDetallesDocumentoDTO(String idDocumento) method");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.obtenerDetallesDocumentoDTO(idDocumento, conexion.getSession());
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idUnidadDocumental   Id de la unidad documental
     */
    public MensajeRespuesta listaDocumentosDTOUnidadDocumental(String idUnidadDocumental) throws Exception {
        logger.info("### Listando las Unidades Documentales listarUnidadDocumental method");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.listaDocumentosDTOUnidadDocumental(idUnidadDocumental, conexion.getSession());
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws BusinessException {
        logger.info("### Ejecutando MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental)");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.detallesUnidadDocumental(idUnidadDocumental, conexion.getSession());
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @param documentoDTOS           Lista de documentos a guardar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, List<DocumentoDTO> documentoDTOS) {
        logger.info("### Ejecutando MensajeRespuesta subirDocumentosUnidadDocumental(unidadDocumentalDTO, documentoDTOS)");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.subirDocumentosUnidadDocumental(unidadDocumentalDTO, documentoDTOS, conexion.getSession());
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @param documentoDTO            Documento a guardar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentoUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO, DocumentoDTO documentoDTO) {
        logger.info("### Ejecutando MensajeRespuesta subirDocumentoUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        logger.info(MSGCONEXION);
        Conexion conexion = contentControl.obtenerConexion();
        return contentControl.subirDocumentoUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO, conexion.getSession());
    }
}
