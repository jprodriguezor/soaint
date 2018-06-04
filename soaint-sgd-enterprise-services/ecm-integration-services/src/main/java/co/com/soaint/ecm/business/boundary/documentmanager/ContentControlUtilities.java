package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentStamper;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

@Log4j2
@Service
final class ContentControlUtilities implements Serializable {

    private static final long serialVersionUID = 155L;

    @Autowired
    private Configuracion configuracion;

    @Autowired
    private ContentControl contentControl;

    @Autowired
    private ContentStamper contentStamper;

    UnidadDocumentalDTO modificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        final String id = unidadDocumentalDTO.getId();
        if (StringUtils.isEmpty(id)) {
            throw new SystemException("Especifique el id de la Unidad Documental");
        }
        final Optional<Folder> optionalFolder = contentControl.getUDFolderById(unidadDocumentalDTO.getId(), session);
        if (!optionalFolder.isPresent()) {
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        Folder folder = optionalFolder.get();
        final CmisObject object = folder.updateProperties(updateProperties(folder, unidadDocumentalDTO));
        return transformarUnidadDocumental((Folder) object);
    }

    Optional<DocumentoDTO> subirDocumentoDtoTemp(DocumentoDTO documentoDTO, Session session) throws SystemException {
        if (ObjectUtils.isEmpty(documentoDTO)) {
            throw new SystemException("No se ha especificado el documento");
        }
        if (StringUtils.isEmpty(documentoDTO.getNombreDocumento())) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        final String codigoDependencia = documentoDTO.getCodigoDependencia();
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }
        if (ObjectUtils.isEmpty(documentoDTO.getDocumento())) {
            throw new SystemException("El documento no contiene informacion");
        }
        final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, codigoDependencia, session);
        if (optionalCarpeta.isPresent()) {
            Folder tmpFolder = optionalCarpeta.get().getFolder();
            if (ObjectUtils.isEmpty(tmpFolder)) {
                throw new SystemException(ConstantesECM.NO_EXISTE_DEPENDENCIA + "'" + codigoDependencia + "'");
            }
            documentoDTO.setNombreDocumento(documentoDTO.getNombreDocumento().trim());
            final ItemIterable<CmisObject> children = tmpFolder.getChildren();
            final Iterator<CmisObject> iterator = children.iterator();

            Folder folder = null;
            while (iterator.hasNext()) {
                final CmisObject next = iterator.next();
                if (ConstantesECM.DOCUMENTOS_POR_ARCHIVAR.equals(next.getName())) {
                    folder = (Folder) next;
                    break;
                }
            }
            if (ObjectUtils.isEmpty(folder)) {
                UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
                dto.setNombreUnidadDocumental(ConstantesECM.DOCUMENTOS_POR_ARCHIVAR);
                dto.setCodigoDependencia(codigoDependencia);
                final Optional<Folder> optionalFolder = crearUnidadDocumentalFolder(dto, session);
                if (optionalFolder.isPresent()) {
                    folder = optionalFolder.get();
                }
            }
            if (!ObjectUtils.isEmpty(folder)) {
                byte[] bytes = documentoDTO.getDocumento();
                Map<String, Object> properties = new HashMap<>();
                properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
                properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento());
                properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentoDTO.getTipoDocumento());
                ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
                Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
                documentoDTO = transformarDocumento(document);
                documentoDTO.setNroRadicado(documentoDTO.getIdDocumento());
                return Optional.of(documentoDTO);
            }
        }
        return Optional.empty();
    }

    List<Folder> getUDListWithIDBy(final String dependencyCode, final Session session) {
        final List<Folder> folderList = new ArrayList<>();
        final String query = "SELECT * FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + dependencyCode + "%'" +
                " AND " + ConstantesECM.CMCOR_UD_ID + " IS NOT NULL" +
                " AND " + ConstantesECM.CMCOR_UD_ID + " <> ''";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        for (QueryResult queryResult :
                queryResults) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
            final String id = udFolder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
            if (!StringUtils.isEmpty(id.trim())) {
                folderList.add(folderList.size(), udFolder);
            }
        }
        return folderList;
    }

    Optional<Folder> crearUnidadDocumentalFolder(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        Optional<Folder> optionalFolder = contentControl.getUDFolderById(unidadDocumentalDTO.getId(), session);
        if (optionalFolder.isPresent()) {
            log.error("Ya existe una unidad documental con el id {}", unidadDocumentalDTO.getId());
            throw new SystemException("Ya existe la unidad documental con id " + unidadDocumentalDTO.getId());
        }

        final String dependenciaCode = unidadDocumentalDTO.getCodigoDependencia();

        if (ObjectUtils.isEmpty(dependenciaCode)) {
            log.error("La Unidad Documental {} no contiene el codigo de la Dependencia", unidadDocumentalDTO);
            throw new SystemException("La Unidad Documental no contiene el codigo de la Dependencia");
        }

        final String nombreUnidadDocumental = unidadDocumentalDTO.getNombreUnidadDocumental();

        if (ObjectUtils.isEmpty(nombreUnidadDocumental)) {
            log.error("La Unidad Documental no contiene Nombre");
            throw new SystemException("La Unidad Documental no contiene Nombre");
        }

        String query = "SELECT * FROM " + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA) +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA) + "'";

        final String codigoSerie = unidadDocumentalDTO.getCodigoSerie();
        final String codigoSubSerie = unidadDocumentalDTO.getCodigoSubSerie();

        ItemIterable<QueryResult> queryResults;
        boolean flag = false;

        if (ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

            queryResults = session.query(query, false);

            if (queryResults.getPageNumItems() == 0) {
                log.error("No existe la dependencia {} en el ECM", dependenciaCode);
                throw new SystemException(ConstantesECM.NO_EXISTE_DEPENDENCIA + "'" + dependenciaCode + "' en el ECM");
            }
            flag = true;
        }
        if (!flag && !ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

            query = "SELECT * FROM " +
                    ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SERIE) + " " +
                    "WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SERIE) + "' " +
                    "AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + codigoSerie + "'";
            flag = true;
        }
        if (!flag && !ObjectUtils.isEmpty(codigoSerie) && !ObjectUtils.isEmpty(codigoSubSerie)) {

            query = "SELECT * FROM " +
                    ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE) + " " +
                    "WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE) + "' " +
                    "AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + codigoSerie + "' " +
                    "AND " + ConstantesECM.CMCOR_SS_CODIGO + " = '" + codigoSubSerie + "'";
            flag = true;
        }
        if (!flag) {
            throw new SystemException("Se debe especificar el codigo de la serie cuando se especifica el de la sub serie");
        }

        queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            QueryResult queryResult = iterator.next();
            String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            Folder tmpFolder = (Folder) session.getObject(session.getObject(objectId));

            final ItemIterable<CmisObject> children = tmpFolder.getChildren();

            for (CmisObject cmisObject :
                    children) {
                String udName = Utilities.reemplazarCaracteresRaros(nombreUnidadDocumental);
                String folderName = Utilities.reemplazarCaracteresRaros(cmisObject.getName());
                if (cmisObject instanceof Folder && udName.equals(folderName)) {
                    log.error("Ya existe una Carpeta con el nombre {}", nombreUnidadDocumental);
                    throw new SystemException("Ya existe la unidad documental de nombre " + nombreUnidadDocumental + " en el ECM");
                }
            }

            final String soporte = !StringUtils.isEmpty(unidadDocumentalDTO.getSoporte()) ?
                    unidadDocumentalDTO.getSoporte() : "Electrónico";
            final boolean inactivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo()) ?
                    unidadDocumentalDTO.getInactivo() : false;
            final boolean cerrada = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada()) ?
                    unidadDocumentalDTO.getCerrada() : false;
            unidadDocumentalDTO.setSoporte(soporte);
            unidadDocumentalDTO.setInactivo(inactivo);
            unidadDocumentalDTO.setCerrada(cerrada);
            final Map<String, Object> props = updateProperties(null, unidadDocumentalDTO);
            props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
            log.info("Making the tmpFolder!!!");
            final Folder folder = tmpFolder.createFolder(props);
            log.info("Make success");
            return Optional.of(folder);
        }
        return Optional.empty();
    }

    List<SedeDTO> getSedeList(final Session session) throws SystemException {
        try {
            final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_BASE, ConstantesECM.CMCOR_UB_CODIGO, "000", session);
            final List<SedeDTO> sedeDTOS = new ArrayList<>();
            if (optionalCarpeta.isPresent()) {
                Folder folder = optionalCarpeta.get().getFolder();
                final ItemIterable<CmisObject> children = folder.getChildren();
                children.forEach(objectChild -> {
                    final String dependencyCode = objectChild.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    SedeDTO baseDTO = new SedeDTO();
                    baseDTO.setNombreBase(objectChild.getName());
                    baseDTO.setCodigoBase(dependencyCode);
                    baseDTO.setCodigoSede(dependencyCode);
                    baseDTO.setNombreSede(objectChild.getName());
                    sedeDTOS.add(sedeDTOS.size(), baseDTO);
                });
            }
            return sedeDTOS;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    Optional<ContenidoDependenciaTrdDTO> getDependenciaTrdDTO(String query, Session session) {
        final ItemIterable<QueryResult> results = session.query(query, false);
        final List<SerieDTO> serieLista = new ArrayList<>();
        final List<SubSerieDTO> subSerieLista = new ArrayList<>();
        final List<OrganigramaDTO> organigramaDTOS = new ArrayList<>();
        results.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(session.createObjectId(objectId));
            final String name = folder.getName();
            if ("F:cmcor:CM_Subserie".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                final SubSerieDTO subSerie = SubSerieDTO.newInstance()
                        .codigoSubSerie(code).nombreSubSerie(name)
                        .codigoBase(code).nombreBase(name).build();
                subSerieLista.add(subSerieLista.size(), subSerie);
            } else if ("F:cmcor:CM_Serie".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                final SerieDTO serieDTO = SerieDTO.newInstance()
                        .codigoSerie(code).nombreSerie(name).codigoBase(code).nombreBase(name).build();
                serieLista.add(serieLista.size(), serieDTO);
            } else if ("F:cmcor:CM_Unidad_Administrativa".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                final OrganigramaDTO organigramaDTO = OrganigramaDTO.newInstance()
                        .codOrg(code).nomOrg(name).codigoBase(code).nombreBase(name).build();
                organigramaDTOS.add(organigramaDTOS.size(), organigramaDTO);
            }
        });
        if (serieLista.isEmpty() && subSerieLista.isEmpty() && organigramaDTOS.isEmpty()) {
            return Optional.empty();
        }
        final ContenidoDependenciaTrdDTO dependenciaTrdDTO = ContenidoDependenciaTrdDTO.newInstance()
                .listaSerie(serieLista).listaSubSerie(subSerieLista).listaDependencia(organigramaDTOS)
                .listaSede(new ArrayList<>()).build();
        return Optional.of(dependenciaTrdDTO);
    }

    String[] getSerieSubSerie(Folder folder, Session session) {
        final String subSerieCode = folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
        Optional<Carpeta> folderBySubSerieCode = Optional.empty();
        if (!StringUtils.isEmpty(subSerieCode)) {
            folderBySubSerieCode = getFolderBy(ConstantesECM.CLASE_SUBSERIE, ConstantesECM.CMCOR_SS_CODIGO, subSerieCode, session);
        }
        final String serieCode = folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
        Optional<Carpeta> folderBySerieCode = Optional.empty();
        if (!StringUtils.isEmpty(serieCode)) {
            folderBySerieCode = getFolderBy(ConstantesECM.CLASE_SERIE, ConstantesECM.CMCOR_SER_CODIGO, serieCode, session);
        }
        final String subSerieName = folderBySubSerieCode.isPresent() ?
                folderBySubSerieCode.get().getFolder().getName() : "";
        final String serieName = folderBySerieCode.isPresent() ?
                folderBySerieCode.get().getFolder().getName() : "";
        return new String[]{serieName, subSerieName};
    }

    String getAbrevDisposition(String disposicion) {
        disposicion = Utilities.reemplazarCaracteresRaros(disposicion);
        FinalDispositionType dispositionType = FinalDispositionType.CONSERVACION_TOTAL;
        if (dispositionType.getName().equals(disposicion)) {
            return dispositionType.getKey();
        }
        dispositionType = FinalDispositionType.DIGITALIZAR;
        if (dispositionType.getName().equals(disposicion)) {
            return dispositionType.getKey();
        }
        dispositionType = FinalDispositionType.ELIMINAR;
        if (dispositionType.getName().equals(disposicion)) {
            return dispositionType.getKey();
        }
        dispositionType = FinalDispositionType.MICROFILMAR;
        if (dispositionType.getName().equals(disposicion)) {
            return dispositionType.getKey();
        }
        dispositionType = FinalDispositionType.SELECCIONAR;
        if (dispositionType.getName().equals(disposicion)) {
            return dispositionType.getKey();
        }
        return "";
    }

    /**
     * Metodo para buscar crear carpetas
     *
     * @param session            Objeto session
     * @param documentoDTO       Objeto qeu contiene los metadatos
     * @param response           Mensaje de respuesta
     * @param bytes              Contenido del documento
     * @param properties         propiedades de carpeta
     * @param carpetaCrearBuscar Carpeta
     */
    void buscarCrearCarpeta(Session session, DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String carpetaCrearBuscar) {
        log.info("MetaDatos: {}", documentoDTO.toString());
        String idDocumento;
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            log.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento principal..");

            if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
                throw new BusinessException("No se ha identificado el codigo de la Dependencia");
            }
            final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO,
                    documentoDTO.getCodigoDependencia(), session);

            if (optionalCarpeta.isPresent()) {
                Carpeta folderAlfresco = optionalCarpeta.get();
                log.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                Carpeta carpetaTarget;

                Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                        .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
                carpetaTarget = getCarpeta(carpetaCrearBuscar, Optional.of(folderAlfresco), year, produccionDocumental);

                idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
                //Creando el mensaje de respuesta
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("Documento añadido correctamente");
                log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);
            } else {
                log.info(ConstantesECM.NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
                response.setCodMensaje("4444");
                response.setMensaje(ConstantesECM.NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
            }

        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje(ConstantesECM.ECM_ERROR_DUPLICADO);
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje("El nombre del documento('" + documentoDTO.getNombreDocumento() + "') no supera la convencion proporcionada por el ECM");
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(e.getMessage());
        }
    }

    /**
     * Metodo para buscar crear carpetas de radicacion de entrada
     *
     * @param session    Objeto session
     * @param documento  Objeto qeu contiene los metadatos
     * @param response   Mensaje de respuesta
     * @param properties propiedades de carpeta
     */
    void buscarCrearCarpetaRadicacion(Session session, DocumentoDTO documento, MensajeRespuesta response, Map<String, Object> properties, String tipoComunicacion) {

        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            log.info("### Se elige la carpeta donde se va a guardar el documento radicado..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento radicado..");
            folderAlfresco = obtenerCarpetaPorNombre(documento.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                log.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                String comunicacionOficial = "";
                String tipoComunicacionSelector;
                tipoComunicacionSelector = getTipoComunicacionSelector(tipoComunicacion);

                if ("EI".equals(tipoComunicacion) || "SI".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_INTERNA;
                } else if ("EE".equals(tipoComunicacion) || "SE".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_EXTERNA;
                }

                //Se busca si existe la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documento.getDependencia())).findFirst();

                log.info("Se obtienen la dependencia referente a la sede: " + documento.getSede());
                if (dependencia.isPresent()) {

                    log.info("Se busca si existe la carpeta de Comunicaciones Oficiales dentro de la dependencia " + documento.getDependencia());

                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    //Obtengo la carpeta de comunicaciones oficiales si existe
                    Optional<Carpeta> comunicacionOficialFolder = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().contains("0231_COMUNICACIONES OFICIALES")).findFirst();

                    crearInsertarCarpetaRadicacion(documento, response, documento.getDocumento(), properties, comunicacionOficial, tipoComunicacionSelector, comunicacionOficialFolder);
                } else {
                    response.setMensaje(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                    response.setCodMensaje("4445");
                    log.info(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                }
            } else {
                response.setMensaje(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
                response.setCodMensaje("4444");
                log.info(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
            }
        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje("El documento ya existe en el ECM");
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        }
    }

    /**
     * Convierte la Interfaz Folder de opencemis a una UnidadDocumentalDTO
     *
     * @param folder Objeto a transformar
     * @return UnidadDocumentalDTO
     */
    UnidadDocumentalDTO transformarUnidadDocumental(Folder folder) {

        final UnidadDocumentalDTO unidadDocumentalDTO = new UnidadDocumentalDTO();
        unidadDocumentalDTO.setId(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
        unidadDocumentalDTO.setAccion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ACCION));
        unidadDocumentalDTO.setDescriptor2(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2));
        unidadDocumentalDTO.setCerrada(folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA));
        unidadDocumentalDTO.setFechaCierre(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE));
        unidadDocumentalDTO.setFechaExtremaInicial(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL));
        unidadDocumentalDTO.setFechaExtremaFinal(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL));
        unidadDocumentalDTO.setSoporte(folder.getPropertyValue(ConstantesECM.CMCOR_UD_SOPORTE));
        unidadDocumentalDTO.setInactivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO));
        unidadDocumentalDTO.setUbicacionTopografica(folder.getPropertyValue(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA));
        unidadDocumentalDTO.setFaseArchivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FASE_ARCHIVO));
        unidadDocumentalDTO.setDescriptor1(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1));
        unidadDocumentalDTO.setCodigoUnidadDocumental(folder.getPropertyValue(ConstantesECM.CMCOR_UD_CODIGO));
        unidadDocumentalDTO.setCodigoSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
        unidadDocumentalDTO.setCodigoSubSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO));
        unidadDocumentalDTO.setCodigoDependencia(folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
        unidadDocumentalDTO.setNombreUnidadDocumental(folder.getPropertyValue(PropertyIds.NAME));
        unidadDocumentalDTO.setObservaciones(folder.getPropertyValue(ConstantesECM.CMCOR_UD_OBSERVACIONES));
        unidadDocumentalDTO.setEstado(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ESTADO));
        unidadDocumentalDTO.setDisposicion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION));
        return unidadDocumentalDTO;
    }

    /**
     * Convierte la Interfaz Document de opencemis a un DocumentoDTO
     *
     * @param document Objeto a transformar
     * @return DocumentoDTO
     */
    DocumentoDTO transformarDocumento(Document document) throws SystemException {
        final DocumentoDTO documentoDTO = new DocumentoDTO();
        String idDoc = document.getId();
        try {
            documentoDTO.setIdDocumento(idDoc.contains(";") ? idDoc.substring(0, idDoc.indexOf(';')) : idDoc);
            documentoDTO.setNroRadicado(document.getPropertyValue(ConstantesECM.CMCOR_NRO_RADICADO));
            documentoDTO.setTipologiaDocumental(document.getPropertyValue(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL));
            documentoDTO.setNombreRemitente(document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_REMITENTE));
            documentoDTO.setNombreDocumento(document.getName());
            documentoDTO.setIdDocumentoPadre(document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
            GregorianCalendar calendar = document.getPropertyValue(PropertyIds.CREATION_DATE);
            documentoDTO.setFechaCreacion(calendar.getTime());
            documentoDTO.setTipoDocumento(document.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE));
            documentoDTO.setTamano(document.getContentStreamLength() + "");
            documentoDTO.setTipoPadreAdjunto(document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
            documentoDTO.setDocumento(getDocumentBytes(document));

            final String nroReferido = document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO);

            if (!StringUtils.isEmpty(nroReferido)) {
                String[] splitRadicado = nroReferido.split(ConstantesECM.SEPARADOR);
                documentoDTO.setNroRadicadoReferido(splitRadicado);
            }
            return documentoDTO;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo para crear el link
     *
     * @param session      Objeto session
     * @param documentoDTO Identificador del dcumento
     * @param carpetaLink  Carpeta donde se va a crear el link
     */
    void crearLink(Session session, DocumentoDTO documentoDTO, Carpeta carpetaLink) {

        log.info("Se entra al metodo crearLink");

        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.BASE_TYPE_ID, BaseTypeId.CMIS_ITEM.value());

        // define a name and a description for the link
        properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento() + ".link");
        properties.put("cmis:description", "Documento Vinculado");
        properties.put(PropertyIds.OBJECT_TYPE_ID, "I:app:filelink");

        //define the destination node reference
        properties.put("cm:destination", "workspace://SpacesStore/" + documentoDTO);
        //Se crea el link
        session.createItem(properties, carpetaLink.getFolder());

        log.info("Se crea el link y se sale del método crearLink");
    }

    Map<String, Object> updateProperties(Folder folder, UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Creating props to make the folder");
        final Map<String, Object> props = new HashMap<>();
        boolean isEmptyFolder = ObjectUtils.isEmpty(folder);

        props.put(ConstantesECM.CMCOR_UD_ID, unidadDocumentalDTO.getId());
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));

        String nameFolder = !ObjectUtils.isEmpty(unidadDocumentalDTO.getNombreUnidadDocumental()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getNombreUnidadDocumental()) : "";
        props.put(PropertyIds.NAME, ((!isEmptyFolder && !ObjectUtils.isEmpty(nameFolder)) || isEmptyFolder) ?
                nameFolder : folder.getPropertyValue(PropertyIds.NAME));

        String accion = !ObjectUtils.isEmpty(unidadDocumentalDTO.getAccion()) ? unidadDocumentalDTO.getAccion() : "";
        props.put(ConstantesECM.CMCOR_UD_ACCION, (!isEmptyFolder && !ObjectUtils.isEmpty(accion) || isEmptyFolder) ?
                accion : folder.getPropertyValue(ConstantesECM.CMCOR_UD_ACCION));

        Calendar fechaInicial = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaInicial()) ? unidadDocumentalDTO.getFechaExtremaInicial() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaInicial) || isEmptyFolder) ?
                fechaInicial : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL));

        Boolean inactivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo()) ? unidadDocumentalDTO.getInactivo() : null;
        props.put(ConstantesECM.CMCOR_UD_INACTIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(inactivo) || isEmptyFolder) ?
                inactivo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO));

        String ubicacionTopografica = !ObjectUtils.isEmpty(unidadDocumentalDTO.getUbicacionTopografica()) ? unidadDocumentalDTO.getUbicacionTopografica() : "";
        props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, (!isEmptyFolder && !ObjectUtils.isEmpty(ubicacionTopografica) || isEmptyFolder) ?
                ubicacionTopografica : folder.getPropertyValue(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA));

        Calendar fechaFinal = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaFinal()) ? unidadDocumentalDTO.getFechaExtremaFinal() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaFinal) || isEmptyFolder) ?
                fechaFinal : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL));

        Calendar fechaCierre = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaCierre()) ? unidadDocumentalDTO.getFechaCierre() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_CIERRE, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaCierre) || isEmptyFolder) ?
                fechaCierre : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE));

        String estado = !ObjectUtils.isEmpty(unidadDocumentalDTO.getEstado()) ? unidadDocumentalDTO.getEstado() : "";
        props.put(ConstantesECM.CMCOR_UD_ESTADO, (!isEmptyFolder && !ObjectUtils.isEmpty(estado) || isEmptyFolder) ?
                estado : folder.getPropertyValue(ConstantesECM.CMCOR_UD_ESTADO));

        String faseArchivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFaseArchivo()) ? unidadDocumentalDTO.getFaseArchivo() : "";
        props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(faseArchivo) || isEmptyFolder) ?
                faseArchivo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FASE_ARCHIVO));

        String soporte = !ObjectUtils.isEmpty(unidadDocumentalDTO.getSoporte()) ? unidadDocumentalDTO.getSoporte() : "";
        props.put(ConstantesECM.CMCOR_UD_SOPORTE, (!isEmptyFolder && !ObjectUtils.isEmpty(soporte) || isEmptyFolder) ?
                soporte : folder.getPropertyValue(ConstantesECM.CMCOR_UD_SOPORTE));

        String descriptor2 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor2()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor2()) : "";
        props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor2) || isEmptyFolder) ?
                descriptor2 : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2));

        String observaciones = !ObjectUtils.isEmpty(unidadDocumentalDTO.getObservaciones()) ? unidadDocumentalDTO.getObservaciones() : "";
        props.put(ConstantesECM.CMCOR_UD_OBSERVACIONES, (!isEmptyFolder && !ObjectUtils.isEmpty(observaciones) || isEmptyFolder) ?
                observaciones : folder.getPropertyValue(ConstantesECM.CMCOR_UD_OBSERVACIONES));

        String disposicion = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDisposicion()) ? unidadDocumentalDTO.getDisposicion() : "";
        props.put(ConstantesECM.CMCOR_UD_DISPOSICION, (!isEmptyFolder && !ObjectUtils.isEmpty(disposicion) || isEmptyFolder) ?
                disposicion : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION));

        String descriptor1 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor1()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor1()) : "";
        props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor1) || isEmptyFolder) ?
                descriptor1 : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1));

        Boolean cerrada = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada()) ? unidadDocumentalDTO.getCerrada() : null;
        props.put(ConstantesECM.CMCOR_UD_CERRADA, (!isEmptyFolder && !ObjectUtils.isEmpty(cerrada) || isEmptyFolder) ?
                cerrada : folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA));

        String udCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoUnidadDocumental()) ? unidadDocumentalDTO.getCodigoUnidadDocumental() : "";
        props.put(ConstantesECM.CMCOR_UD_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(udCodigo) || isEmptyFolder) ?
                udCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_CODIGO));

        String serCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSerie()) ? unidadDocumentalDTO.getCodigoSerie() : "";
        props.put(ConstantesECM.CMCOR_SER_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(serCodigo) || isEmptyFolder) ?
                serCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));

        String ssCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSubSerie()) ? unidadDocumentalDTO.getCodigoSubSerie() : "";
        props.put(ConstantesECM.CMCOR_SS_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(ssCodigo) || isEmptyFolder) ?
                ssCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO));

        String depCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoDependencia()) ? unidadDocumentalDTO.getCodigoDependencia() : "";
        props.put(ConstantesECM.CMCOR_DEP_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(depCodigo) || isEmptyFolder) ?
                depCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));

        String sedeCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSede()) ? unidadDocumentalDTO.getCodigoSede() : "";
        props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, (!isEmptyFolder && !ObjectUtils.isEmpty(sedeCodigo) || isEmptyFolder) ?
                sedeCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_UAP));

        try {
            if (!isEmptyFolder && !props.isEmpty()) {
                folder.updateProperties(props);
            }
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
        return props;
    }

    /**
     * Metodo para actualizar el nombre de la carpeta
     *
     * @param carpeta Carpeta a la cual se le va a actualizar el nombre
     * @param nombre  Nuevo nombre de la carpeta
     */
    void actualizarNombreCarpeta(Carpeta carpeta, String nombre) {
        log.info("### Actualizando nombre folder: " + nombre);
        try {
            carpeta.getFolder().rename(nombre);
        } catch (Exception e) {
            log.error("*** Error al actualizar nombre folder *** ", e);
        }
    }

    /**
     * Metodo que devuelve la carpeta padre de la carpeta que se le pase.
     *
     * @param folderFather Carpeta padre
     * @param codFolder    Codigo de la carpeta que se le va a chequear la carpeta padre
     * @return Carpeta padre
     */
    Carpeta chequearCapetaPadre(Carpeta folderFather, String codFolder) {
        Carpeta folderReturn = null;
        Conexion conexion = contentControl.obtenerConexion();
        List<Carpeta> listaCarpeta = obtenerCarpetasHijasDadoPadre(folderFather);

        Iterator<Carpeta> iterator;
        if (!ObjectUtils.isEmpty(listaCarpeta)) {
            iterator = listaCarpeta.iterator();
            while (iterator.hasNext()) {
                Carpeta aux = iterator.next();
                Carpeta carpeta = obtenerCarpetaPorNombre(aux.getFolder().getName(), conexion.getSession());
                String description = carpeta.getFolder().getDescription();
                if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodDependencia", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SERIE))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSerie", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE))) {
                    log.info("Entro a clase subserie cargando los valores");
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSubserie", folderReturn);
                }
            }
        }
        return folderReturn;
    }

    byte[] getDocumentBytes(Document document) throws SystemException {
        try {
            final File file = convertInputStreamToFile(document.getContentStream());
            return FileUtils.readFileToByteArray(file);
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    ItemIterable<QueryResult> getPrincipalAdjuntosQueryResults(Session session, DocumentoDTO documento) {
        //Obtener el documentosAdjuntos
        String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado";
        boolean where = false;
        if (!ObjectUtils.isEmpty(documento.getIdDocumento())) {
            where = true;
            query += " WHERE " + PropertyIds.OBJECT_ID + " = '" + documento.getIdDocumento() + "'" +
                    " OR " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + documento.getIdDocumento() + "'";
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicado())) {
            query += (where ? " AND " : " WHERE ") + ConstantesECM.CMCOR_NRO_RADICADO + " = '" + documento.getNroRadicado() + "'";
            where = true;
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicadoReferido())) {
            String[] nroRadicadoReferidoArray = documento.getNroRadicadoReferido();
            StringBuilder radicadoReferido = new StringBuilder();
            String separator = ConstantesECM.SEPARADOR;
            for (int i = 0; i < nroRadicadoReferidoArray.length; i++) {
                if (i == nroRadicadoReferidoArray.length - 1) {
                    separator = "";
                }
                radicadoReferido.append(nroRadicadoReferidoArray[i]).append(separator);
            }
            query += (where ? " AND " : " WHERE ") + "(" + ConstantesECM.CMCOR_NUMERO_REFERIDO + " LIKE '%" + radicadoReferido + ConstantesECM.SEPARADOR + "%'";

        }
        return session.query(query, false);
    }

    List<UnidadDocumentalDTO> listarUnidadesDocumentales(List<String> disposicionList, UnidadDocumentalDTO dto, Session session) throws SystemException {
        try {
            String query = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL);
            if (!StringUtils.isEmpty(dto.getCodigoDependencia()) || !StringUtils.isEmpty(dto.getCodigoSede())) {
                query += " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + dto.getCodigoDependencia() + "%'";
            }
            final String where = "WHERE";
            if (!StringUtils.isEmpty(dto.getCodigoSerie())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_SER_CODIGO + " LIKE '" + dto.getCodigoSerie() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getCodigoSubSerie())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_SS_CODIGO + " LIKE '" + dto.getCodigoSubSerie() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getDescriptor1())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DESCRIPTOR_1 + " LIKE '%" + dto.getDescriptor1() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getDescriptor2())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DESCRIPTOR_2 + " LIKE '%" + dto.getDescriptor2() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getNombreUnidadDocumental())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + PropertyIds.NAME + " LIKE '%" + dto.getNombreUnidadDocumental() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getId())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_ID + " LIKE '%" + dto.getId() + "%'";
            }
            if (!ObjectUtils.isEmpty(dto.getSoporte())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_SOPORTE + " LIKE '" + dto.getSoporte() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getAccion())) {
                AccionUsuario accionUsuario = AccionUsuario.valueOf(dto.getAccion().toUpperCase());
                return listarUnidadesDocumentalesPorAccion(accionUsuario, query, session);
            }
            if (!ObjectUtils.isEmpty(dto.getEstado())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_ESTADO + " = '" + dto.getEstado() + "'";
            }
            if (!ObjectUtils.isEmpty(disposicionList)) {
                final StringBuilder in = new StringBuilder();
                for (final String disposition :
                        disposicionList) {
                    final String comma = in.length() == 0 ? "" : ",";
                    FinalDispositionType dispositionType = FinalDispositionType.SELECCIONAR;
                    if (dispositionType.getKey().equals(disposition)) {
                        in.append(comma).append("'").append(dispositionType.getName()).append("'");
                        continue;
                    }
                    dispositionType = FinalDispositionType.ELIMINAR;
                    if (dispositionType.getKey().equals(disposition)) {
                        in.append(comma).append("'").append(dispositionType.getName()).append("'");
                        continue;
                    }
                    dispositionType = FinalDispositionType.MICROFILMAR;
                    if (dispositionType.getKey().equals(disposition)) {
                        in.append(comma).append("'").append(dispositionType.getName()).append("'");
                        continue;
                    }
                    dispositionType = FinalDispositionType.DIGITALIZAR;
                    if (dispositionType.getKey().equals(disposition)) {
                        in.append(comma).append("'").append(dispositionType.getName()).append("'");
                        continue;
                    }
                    dispositionType = FinalDispositionType.CONSERVACION_TOTAL;
                    in.append(comma).append("'").append(dispositionType.getName()).append("'");
                }
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DISPOSICION + " IN (" + in.toString() + ")";
                query += " AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'true'" +
                        " AND " + ConstantesECM.CMCOR_UD_FASE_ARCHIVO + " LIKE '" + PhaseType.ARCHIVO_CENTRAL.getName() + "%'";
                log.info("Ejecutar consulta {}", query);
            } else {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_CERRADA + " = 'false'";
            }
            return listarUnidadesDocumentales(query, dto, session);

        } catch (Exception e) {
            log.error("Error al Listar las Unidades Documentales");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo que crea carpetas dentro de Alfresco
     *
     * @param folder          Objeto carpeta que contiene un Folder de Alfresco
     * @param nameOrg         Nombre de la carpeta
     * @param codOrg          Codigo de la carpeta que se va a crear
     * @param classDocumental Clase documental que tiene la carpeta que se va a crar.
     * @param folderFather    Carpeta dentro de la cual se va a crear la carpeta
     * @return Devuelve la carpeta creada dentro del objeto Carpeta
     */
    Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg,
                         String classDocumental, Carpeta folderFather, String idOrgOfc) {
        Carpeta newFolder = null;
        try {

            log.info("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map<String, Object> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg
            props.put(PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre
            switch (classDocumental) {
                case ConstantesECM.CLASE_BASE:
                    llenarPropiedadesCarpeta(ConstantesECM.CLASE_BASE, props, codOrg);
                    break;
                case ConstantesECM.CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_DEP_CODIGO, ConstantesECM.CLASE_DEPENDENCIA, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SER_CODIGO, ConstantesECM.CLASE_SERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SS_CODIGO, ConstantesECM.CLASE_SUBSERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_UNIDAD_DOCUMENTAL:

                    final Folder tmpFolder = folderFather.getFolder();

                    final String depCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    String depCode = !ObjectUtils.isEmpty(depCodeValue) ? depCodeValue : "";

                    final String serieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                    String serieCode = !ObjectUtils.isEmpty(serieCodeValue) ? serieCodeValue : "";

                    final String subSerieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                    String subSerieCode = !ObjectUtils.isEmpty(subSerieCodeValue) ? subSerieCodeValue : "";

                    final String codUnidadAdminPadreValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_UAP);
                    String codUnidadAdminPadre = !ObjectUtils.isEmpty(codUnidadAdminPadreValue) ? codUnidadAdminPadreValue : "";

                    props.put(ConstantesECM.CMCOR_UD_ID, "");
                    props.put(ConstantesECM.CMCOR_UD_ACCION, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, "");
                    props.put(ConstantesECM.CMCOR_UD_CERRADA, false);
                    props.put(ConstantesECM.CMCOR_UD_SOPORTE, "");
                    props.put(ConstantesECM.CMCOR_UD_INACTIVO, false);
                    props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, "");
                    props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, "");
                    props.put(ConstantesECM.CMCOR_UD_CODIGO, "");
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, !"".equals(subSerieCode) ? subSerieCode :
                            !"".equals(serieCode) ? serieCode :
                                    !"".equals(depCode) ? depCode : codUnidadAdminPadre);
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO, depCode);
                    props.put(ConstantesECM.CMCOR_SER_CODIGO, serieCode);
                    props.put(ConstantesECM.CMCOR_SS_CODIGO, subSerieCode);
                    props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    break;
                default:
                    break;
            }
            //Se crea la carpeta dentro de la carpeta folder
            log.info("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta();
            newFolder.setFolder(folder.getFolder().createFolder(props));
            log.info("---------------------Carpeta: " + newFolder.getFolder().getName() + " creada--------------");
        } catch (Exception e) {
            log.error("*** Error al crear carpeta *** ", e);
        }
        return newFolder;
    }

    /**
     * Metodo para retornar el archivo
     *
     * @param documentoDTO   Objeto que contiene los metadatos
     * @param versionesLista Listado por el que se va a buscar
     * @param version        Version del documento que se esta buscando
     * @return Objeto file
     * @throws IOException Tipo de excepcion
     */
    File getFile(DocumentoDTO documentoDTO, ArrayList<DocumentoDTO> versionesLista, Document version) throws SystemException {
        if (version.getVersionLabel().equals(documentoDTO.getVersionLabel())) {
            documentoDTO.setNombreDocumento(version.getName());
            versionesLista.add(documentoDTO);
            return convertInputStreamToFile(version.getContentStream());
        }
        return null;
    }

    /**
     * Metodo que obtiene la carpeta dado el nombre
     *
     * @param nombreCarpeta NOmbre de la carpeta que se va a buscar
     * @param session       objeto de conexion al Alfresco
     * @return Retorna la Carpeta que se busca
     */
    Carpeta obtenerCarpetaPorNombre(String nombreCarpeta, Session session) {
        Carpeta folder = new Carpeta();
        try {
            String queryString = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmis:folder" +
                    " WHERE " + PropertyIds.NAME + " = '" + nombreCarpeta + "'" +
                    " and (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Serie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Subserie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Documental')";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            log.error("*** Error al obtenerCarpetas *** ", e);
        }
        return folder;
    }

    /**
     * @param informationArray Arreglo que trae el nombre de la carpeta para formatearlo para ser usado por el ECM
     * @param formatoConfig    Contiene el formato que se le dara al nombre
     * @return Nombre formateado
     */
    String formatearNombre(String[] informationArray, String formatoConfig) {
        String formatoCadena;
        StringBuilder formatoFinal = new StringBuilder();
        try {
            formatoCadena = configuracion.getPropiedad(formatoConfig);
            String[] formatoCadenaArray = formatoCadena.split("");
            for (String aFormatoCadenaArray : formatoCadenaArray) {

                String nomSerie = "3";
                String idOrgAdm = "0";
                String idOrgOfc = "1";
                String codSerie = "2";
                String codSubserie = "4";
                String nomSubserie = "5";
                if (aFormatoCadenaArray.equals(idOrgAdm)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgAdm)]);
                } else if (aFormatoCadenaArray.equals(idOrgOfc)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgOfc)]);
                } else if (aFormatoCadenaArray.equals(codSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSerie)]);
                } else if (aFormatoCadenaArray.equals(nomSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSerie)]);
                } else if (aFormatoCadenaArray.equals(codSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSubserie)]);
                } else if (aFormatoCadenaArray.equals(nomSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSubserie)]);
                } else if (isNumeric(aFormatoCadenaArray)) {
                    //El formato no cumple con los requerimientos minimos
                    log.info("El formato no cumple con los requerimientos.");
                    formatoFinal = new StringBuilder();
                    break;
                } else {
                    formatoFinal.append(aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            log.error("*** Error al formatear nombre *** ", e);
        }
        return !ObjectUtils.isEmpty(formatoFinal) ? formatoFinal.toString() : "";
    }

    /**
     * Convierte contentStream a File
     *
     * @param contentStream contentStream
     * @return Un objeto File
     * @throws SystemException En caso de error
     */
    File convertInputStreamToFile(ContentStream contentStream) throws SystemException {
        try {
            File file = File.createTempFile(contentStream.getFileName(), "");
            try (InputStream inputStream = contentStream.getStream(); OutputStream out = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (IOException e) {
                log.error("Error al convertir el contentStream a File", e);
            }
            file.deleteOnExit();
            return file;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    Map<String, Object> obtenerPropiedadesDocumento(Document document) {
        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, document.getName());
        map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        map.put(PropertyIds.OBJECT_ID, document.getId().split(";")[0]);
        map.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
        map.put(ConstantesECM.CMCOR_NRO_RADICADO, document.getPropertyValue(ConstantesECM.CMCOR_NRO_RADICADO));
        map.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_REMITENTE));
        map.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
        map.put(ConstantesECM.CMCOR_NUMERO_REFERIDO, document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO));
        return map;
    }

    void eliminarDocumentosAnexos(String idDoc, Session session) throws SystemException {
        log.info("Eliminar todos los documentos anexos a {}", idDoc);
        final String query = "select * from cmcor:CM_DocumentoPersonalizado" +
                " where " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + idDoc + "'";
        try {
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            for (QueryResult queryResult :
                    queryResults) {
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                contentControl.eliminardocumento(objectId, session);
            }
        } catch (Exception e) {
            log.error("Ocurrio un error al eliminar documentos anexos");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    void estamparEtiquetaRadicacion(DocumentoDTO documentoDTO, Session session) throws SystemException {
        try {
            final String nroRadicado = documentoDTO.getNroRadicado();
            if (StringUtils.isEmpty(nroRadicado)) {
                throw new SystemException("No se especifico el numero de radicado del documento");
            }
            byte[] documentBytes = documentoDTO.getDocumento();
            if (ObjectUtils.isEmpty(documentBytes)) {
                throw new SystemException("No se especifico el contenido del archivo");
            }
            String idDocument = documentoDTO.getIdDocumento();
            if (StringUtils.isEmpty(idDocument)) {
                saveStamperImageFile(documentBytes, nroRadicado);
                return;
            }

            idDocument = idDocument.indexOf(';') != -1 ? idDocument.split(";")[0] : idDocument;
            CmisObject cmisObject = session.getObject(session.createObjectId(idDocument));

            Document documentECM = (Document) cmisObject;
            String docMimeType = documentECM.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE);
            final Folder folder = getFolderFrom(documentECM);
            if (null == folder) {
                throw new SystemException("Ocurrio un error al estampar la etiqueta de radicacion");
            }

            byte[] imageBytes;
            DocumentMimeType mimeType = DocumentMimeType.APPLICATION_PDF;
            Document documentImg = null;
            if (DocumentMimeType.APPLICATION_HTML.getType().equals(docMimeType)) {
                imageBytes = documentBytes;
                documentBytes = getDocumentBytes(documentECM);
                mimeType = DocumentMimeType.APPLICATION_HTML;
            } else {
                documentImg = getStamperImage(nroRadicado);
                if (null == documentImg) {
                    throw new SystemException("No existe imagen con numero de radicado " + nroRadicado);
                }
                final File file = convertInputStreamToFile(documentImg.getContentStream());
                imageBytes = imageBytes(file);
            }

            final byte[] stampedDocument = contentStamper
                    .getStampedDocument(imageBytes, documentBytes, mimeType);

            String docName = documentECM.getName();
            final String pdfSufix = ".pdf";
            String sufix = "";
            if (docName.endsWith(".htm")) {
                sufix = ".htm";
            }
            if (docName.endsWith(".html")) {
                sufix = ".html";
            }
            if (docName.endsWith(".xhtml")) {
                sufix = ".xhtml";
            }
            docName = "".equals(sufix) ? docName + pdfSufix : docName.replace(sufix, pdfSufix);

            documentoDTO.setNombreDocumento(docName);

            Map<String, Object> properties = obtenerPropiedadesDocumento(documentECM);
            properties.put(ConstantesECM.CMCOR_NRO_RADICADO, documentoDTO.getNroRadicado());
            properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento());
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, DocumentMimeType.APPLICATION_PDF.getType());
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, "Principal");

            documentECM.delete(false);

            final ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(),
                    BigInteger.valueOf(stampedDocument.length), DocumentMimeType.APPLICATION_PDF.getType(), new ByteArrayInputStream(stampedDocument));
            folder.createDocument(properties, contentStream, VersioningState.MAJOR);

            if (null != documentImg) {
                documentImg.delete();
            }

        } catch (Exception e) {
            log.error("Error al estampar la etiqueta de radicacion");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    Folder getFolderFrom(Document document) {
        final List<Folder> parents = document.getParents();
        for (Folder itFoldr : parents) {
            if (itFoldr.getType().getId().startsWith("F:cmcor:CM_Unidad_D")) {
                String idUd = itFoldr.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
                if (StringUtils.isEmpty(idUd) || "".equals(idUd.trim())) {
                    return itFoldr;
                }
            }
        }
        return null;
    }

    private Optional<Carpeta> getFolderBy(final String classType, final String propertyName,
                                          final String value, final Session session) {
        String query = "SELECT * FROM";
        switch (classType) {
            case ConstantesECM.CLASE_BASE:
                query += " cmcor:CM_Unidad_Base WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'";
                break;
            case ConstantesECM.CLASE_SEDE:
            case ConstantesECM.CLASE_DEPENDENCIA:
                query += " cmcor:CM_Unidad_Administrativa WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'";
                break;
            case ConstantesECM.CLASE_SERIE:
                query += " cmcor:CM_Serie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Serie'";
                break;
            case ConstantesECM.CLASE_SUBSERIE:
                query += " cmcor:CM_Subserie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Subserie'";
                break;
            default:
                return Optional.empty();
        }
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            final QueryResult next = iterator.next();
            final String objectId = next.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
            final Carpeta carpeta = new Carpeta();
            carpeta.setFolder((Folder) cmisObject);
            return Optional.of(carpeta);
        }
        return Optional.empty();
    }

    DocumentoDTO subirDocumentoPrincipalPD(DocumentoDTO documentoDTO, Session session) throws SystemException {
        if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
            throw new SystemException("No se ha identificado el codigo de la Dependencia");
        }
        final String nombreDoc = documentoDTO.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        final byte[] bytes = documentoDTO.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }
        final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO,
                documentoDTO.getCodigoDependencia(), session);
        if (!optionalCarpeta.isPresent()) {
            throw new SystemException(ConstantesECM.NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
        }
        Carpeta carpeta = optionalCarpeta.get();
        final String nameFolder = SelectorType.PD.getSelectorName();
        final Optional<Folder> optionalFolder = sonFolderExistsFrom(carpeta.getFolder(), nameFolder);
        if (!optionalFolder.isPresent()) {
            carpeta = crearCarpeta(carpeta, nameFolder, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpeta, null);
        } else {
            carpeta.setFolder(optionalFolder.get());
        }
        return crearDocumento(carpeta, documentoDTO);
    }

    DocumentoDTO subirDocumentoPrincipalRadicacion(DocumentoDTO documentoDTO, SelectorType selectorType, Session session) throws SystemException {
        final String nombreDoc = documentoDTO.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        final byte[] bytes = documentoDTO.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }

        final String query = "SELECT * FROM cmcor:CM_Subserie" +
                " WHERE " + PropertyIds.NAME + " LIKE '%" + selectorType.getFatherFolderName() + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:cmcor:CM_Subserie'";

        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            final QueryResult next = iterator.next();
            final String objectId = next.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            Folder communicationFolder = (Folder) session.getObject(session.createObjectId(objectId));
            Carpeta carpeta = new Carpeta();
            carpeta.setFolder(communicationFolder);
            Optional<Folder> optionalFolder = sonFolderExistsFrom(communicationFolder, selectorType.getSelectorName());
            if (!optionalFolder.isPresent()) {
                carpeta = crearCarpeta(carpeta, selectorType.getSelectorName(), "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpeta, null);
            } else {
                carpeta.setFolder(optionalFolder.get());
            }
            return crearDocumento(carpeta, documentoDTO);
        }
        throw new SystemException("En la dependencia 10001040 no existe la carpeta " + selectorType.getFatherFolderName());
    }

    private DocumentoDTO crearDocumento(Carpeta carpeta, DocumentoDTO documento) throws SystemException {
        final Map<String, Object> properties = new HashMap<>();
        final String nombreDoc = documento.getNombreDocumento();
        final byte[] bytes = documento.getDocumento();
        final String documentMimeType = StringUtils.isEmpty(documento.getTipoDocumento()) ?
                DocumentMimeType.APPLICATION_PDF.getType() : documento.getTipoDocumento();
        ContentStream contentStream = new ContentStreamImpl(nombreDoc,
                BigInteger.valueOf(bytes.length), documentMimeType, new ByteArrayInputStream(bytes));

        properties.put(PropertyIds.NAME, nombreDoc);
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, !StringUtils.isEmpty(documento.getTipoPadreAdjunto())
                ? documento.getTipoPadreAdjunto() : "Principal");
        properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, documento.getIdDocumentoPadre());
        properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentMimeType);
        properties.put(ConstantesECM.CMCOR_NRO_RADICADO, documento.getNroRadicado());
        properties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, documento.getNombreRemitente());
        try {
            final Folder folder = carpeta.getFolder();
            final Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
            return transformarDocumento(document);
        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("El documento ya existe en el ECM")
                    .withRootException(ccaee)
                    .buildSystemException();
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private Optional<Folder> sonFolderExistsFrom(Folder folder, String selectorName) {
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject :
                children) {
            if (cmisObject instanceof Folder && cmisObject.getName().equals(selectorName)) {
                return Optional.of((Folder) cmisObject);
            }
        }
        return Optional.empty();
    }


    /**
     * Metodo que retorna true en caso de que la cadena que se le pasa es numerica y false si no.
     *
     * @param cadena Cadena de texto que se le pasa al metodo
     * @return Retorna true o false
     */
    private boolean isNumeric(String cadena) {
        return cadena.matches("[+-]?\\d*(\\.\\d+)?") && "".equals(cadena) == Boolean.FALSE;
    }

    private List<UnidadDocumentalDTO> listarUnidadesDocumentales(String query, UnidadDocumentalDTO dto, Session session) {
        log.info("Executing query {}", query);
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = new ArrayList<>();
        for (QueryResult queryResult :
                queryResults) {
            final String idUnidadDocumental = queryResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_UD_ID);
            if (!StringUtils.isEmpty(idUnidadDocumental) && !idUnidadDocumental.trim().isEmpty()) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder folder = (Folder) session.getObject(session.getObject(objectId));
                if (ObjectUtils.isEmpty(dto) || hasDatesInRange(folder, dto)) {
                    unidadDocumentalDTOS.add(unidadDocumentalDTOS.size(), transformarUnidadDocumental(folder));
                }
            }
        }
        return unidadDocumentalDTOS;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param accionUsuario accion a realizar(abrir, cerrar, reactivar)
     * @param query         una previa consulta de seleccion de valores del dto UD
     * @param session       Obj coneccion de Alfresco
     * @return List<UnidadDocumentalDTO> Lst
     */
    private List<UnidadDocumentalDTO> listarUnidadesDocumentalesPorAccion(AccionUsuario accionUsuario, String query, Session session) throws BusinessException {
        try {
            query += (!query.contains("WHERE") ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_FECHA_INICIAL + " IS NOT NULL";
            switch (accionUsuario) {
                case ABRIR:
                case REACTIVAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'true' AND " + ConstantesECM.CMCOR_UD_FECHA_FINAL + " IS NOT NULL" +
                            " AND " + ConstantesECM.CMCOR_UD_FECHA_CIERRE + " IS NOT NULL AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'true'";
                    break;
                case CERRAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'false' AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'false'";
                    break;
            }
            final List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(query, null, session);
            final List<UnidadDocumentalDTO> udTmp = new ArrayList<>();
            unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
                final String soporte = unidadDocumentalDTO.getSoporte();
                if (!StringUtils.isEmpty(soporte) && !soporte.trim().isEmpty()) {
                    udTmp.add(udTmp.size(), unidadDocumentalDTO);
                }
            });
            return udTmp;

        } catch (Exception e) {
            throw new BusinessException("ECM ERROR: " + e.getMessage());
        }
    }

    /**
     * Metodo auxuliar para devolver la carpeta padre
     *
     * @param codFolder Codigo de carpeta
     * @param aux       Carpeta por la cual se realiza la busqueda
     * @param metadato  Propiedad por la cual se filtra
     * @return Carpeta padre
     */
    private Carpeta getCarpeta(String codFolder, Carpeta aux, String metadato, Carpeta folderReturn) {
        Carpeta folderAux = folderReturn;
        final Object propertyValue = aux.getFolder().getPropertyValue(ConstantesECM.CMCOR + configuracion.getPropiedad(metadato));
        if (!ObjectUtils.isEmpty(propertyValue) &&
                propertyValue.equals(codFolder)) {
            folderAux = aux;
        }
        return folderAux;
    }

    /**
     * Metodo que verifica la existencia de fechas en el dto y el rango de seleccion
     *
     * @param folder {@link Folder}
     * @param dto    {@link UnidadDocumentalDTO}
     * @return boolean {@link Boolean}
     */
    private boolean hasDatesInRange(Folder folder, UnidadDocumentalDTO dto) {

        Calendar dtoFechaCierre = dto.getFechaCierre();
        Calendar folderFechaCierre = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE);

        Calendar dtoFechaInicial = dto.getFechaExtremaInicial();
        Calendar folderFechaInicial = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);

        Calendar dtoFechaFinal = dto.getFechaExtremaFinal();
        Calendar folderFechaFinal = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL);

        if (dtoFechaCierre == null && dtoFechaInicial == null && dtoFechaFinal == null) {
            return true;
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaInicial != null && folderFechaInicial != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null)) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, dtoFechaFinal) <= 0);
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaInicial != null && folderFechaInicial != null) && dtoFechaFinal == null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) >= 0);
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null) && dtoFechaInicial == null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) <= 0);
        }
        if (dtoFechaCierre != null && folderFechaCierre != null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0);
        }
        if ((dtoFechaInicial != null && folderFechaInicial != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null)) {
            return (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, dtoFechaFinal) <= 0);
        }
        if (dtoFechaInicial != null && folderFechaInicial != null) {
            return (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) >= 0);
        }
        return ((dtoFechaFinal != null && folderFechaFinal != null) &&
                (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) <= 0));
    }

    /**
     * Metodo que devuelve las carpetas hijas de una carpeta
     *
     * @param carpetaPadre Carpeta a la cual se le van a buscar las carpetas hijas
     * @return Lista de carpetas resultantes de la busqueda
     */
    private List<Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) {
        log.info("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder().getName());
        List<Carpeta> listaCarpetas = new ArrayList<>();

        try {
            ItemIterable<CmisObject> listaObjetos = carpetaPadre.getFolder().getChildren();
            listaCarpetas = new ArrayList<>();
            //Lista de carpetas hijas
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder && contentItem.getType().getId().startsWith("F:cmcor")) {
                    Carpeta folder = new Carpeta();
                    folder.setFolder((Folder) contentItem);
                    listaCarpetas.add(folder);
                }
            }
        } catch (Exception e) {
            log.error("*** Error al obtener Carpetas Hijas dado padre*** ", e);
        }
        return listaCarpetas;
    }

    private String getTipoComunicacionSelector(String tipoComunicacion) {
        switch (tipoComunicacion) {
            case "EI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;

            case "SI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_ENVIADAS;

            case "EE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_RECIBIDAS;

            case "SE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_ENVIADAS;
            default:
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;
        }
    }

    private void crearInsertarCarpetaRadicacion(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String comunicacionOficial, String tipoComunicacionSelector, Optional<Carpeta> comunicacionOficialFolder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Carpeta carpetaTarget = new Carpeta();
        String idDocumento;
        if (comunicacionOficialFolder.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialFolder.get().getFolder().getName());

            List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(comunicacionOficialFolder.get());

            Optional<Carpeta> comunicacionOficialInOut = carpetasDeComunicacionOficial.stream()
                    .filter(p -> p.getFolder().getName().contains(comunicacionOficial)).findFirst();

            if (!comunicacionOficialInOut.isPresent()) {

                Carpeta carpetaCreada = crearCarpeta(comunicacionOficialFolder.get(), comunicacionOficial, "11", ConstantesECM.CLASE_SUBSERIE, comunicacionOficialFolder.get(), null);
                if (carpetaCreada != null) {
                    log.info(ConstantesECM.EXISTE_CARPETA + carpetaCreada.getFolder().getName());
                    List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(carpetaCreada);

                    Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                            .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                    carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(carpetaCreada, tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpetaCreada, null));
                }

            } else {
                log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialInOut.get().getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(comunicacionOficialInOut.get());

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(comunicacionOficialInOut.get(), tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, comunicacionOficialInOut.get(), null));
            }
            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("Documento añadido correctamente");
            log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);

        } else {
            response.setCodMensaje("3333");
            response.setMensaje("En esta sede y dependencia no esta permitido realizar radicaciones");
        }
    }

    private String crearDocumentoDevolverId(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, List<DocumentoDTO> documentoDTOList, Carpeta carpetaTarget) {
        String idDocumento;
        log.info("Se llenan los metadatos del documento a crear");
        ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));

        if (documentoDTO.getNroRadicado() != null) {
            properties.put(ConstantesECM.CMCOR_NRO_RADICADO, documentoDTO.getNroRadicado());
        }
        if (documentoDTO.getNombreRemitente() != null) {
            properties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, documentoDTO.getNombreRemitente());
        }
        if (documentoDTO.getTipologiaDocumental() != null) {
            properties.put(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL, documentoDTO.getTipologiaDocumental());
        }
        log.info(ConstantesECM.AVISO_CREA_DOC);
        final String docName = StringUtils.isEmpty(documentoDTO.getNombreDocumento()) ?
                "" : documentoDTO.getNombreDocumento().trim();
        properties.put(PropertyIds.NAME, docName);
        documentoDTO.setNombreDocumento(docName);
        Document newDocument = carpetaTarget.getFolder().createDocument(properties, contentStream, VersioningState.MAJOR);

        idDocumento = newDocument.getId();
        String[] parts = idDocumento.split(";");
        idDocumento = parts[0];
        documentoDTO.setVersionLabel(newDocument.getVersionLabel());
        documentoDTO.setIdDocumento(idDocumento);
        documentoDTOList.add(documentoDTO);
        response.setDocumentoDTOList(documentoDTOList);
        return idDocumento;
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param tipoCarpeta tipo de carpeta
     * @param clase       clase de la carpeta
     * @param props       objeto de propiedades
     * @param codOrg      codigo
     */
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map<String, Object> props, String codOrg, Carpeta folderFather, String idOrgOfc) {

        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(tipoCarpeta, codOrg);

        if (ConstantesECM.CMCOR_SS_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_SER_CODIGO, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else if (ConstantesECM.CMCOR_SER_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else {
            props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, codOrg);
            props.put(ConstantesECM.CMCOR_DEP_CODIGO, codOrg);
        }
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param clase  clase de la carpeta
     * @param props  objeto de propiedades
     * @param codOrg codigo
     */
    private void llenarPropiedadesCarpeta(String clase, Map<String, Object> props, String codOrg) {
        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(ConstantesECM.CMCOR_UB_CODIGO, codOrg);
    }

    private Carpeta getCarpeta(String carpetaCrearBuscar, Optional<Carpeta> dependencia, int year, Optional<Carpeta> produccionDocumental) {
        Carpeta carpetaTarget = null;
        if (produccionDocumental.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + carpetaCrearBuscar + year);
            carpetaTarget = produccionDocumental.get();
        } else {
            log.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
            if (dependencia.isPresent()) {
                carpetaTarget = crearCarpeta(dependencia.get(), carpetaCrearBuscar + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, dependencia.get(), null);
            }
        }
        return carpetaTarget;
    }

    private void saveStamperImageFile(byte[] bytes, String filename) {
        Session session = contentControl.obtenerConexion().getSession();
        Folder rootFolder = session.getRootFolder();
        Folder folderImage = getStamperImageFolder();
        if (null == folderImage) {
            final Map<String, Object> map = new HashMap<>();
            map.put(PropertyIds.NAME, "Images");
            map.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            folderImage = rootFolder.createFolder(map);
        }
        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, "tag_" + filename + "_temp.png");
        map.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        map.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, DocumentMimeType.APPLICATION_ICON.getType());
        ContentStream stream = new ContentStreamImpl(filename, BigInteger.valueOf(bytes.length),
                DocumentMimeType.APPLICATION_ICON.getType(), new ByteArrayInputStream(bytes));
        folderImage.createDocument(map, stream, VersioningState.MAJOR);
    }

    private Folder getStamperImageFolder() {
        Session session = contentControl.obtenerConexion().getSession();
        Folder rootFolder = session.getRootFolder();
        final ItemIterable<CmisObject> children = rootFolder.getChildren();
        for (CmisObject cmisObject :
                children) {
            if ("Images".equals(cmisObject.getName())) {
                return (Folder) cmisObject;
            }
        }
        return null;
    }

    private Document getStamperImage(String filename) {
        Folder folder = getStamperImageFolder();
        if (null != folder) {
            final ItemIterable<CmisObject> children = folder.getChildren();
            for (CmisObject cmisObject :
                    children) {
                if (cmisObject.getName().contains(filename)) {
                    return (Document) cmisObject;
                }
            }
        }
        return null;
    }

    private byte[] imageBytes(File imgFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imgFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( originalImage, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }
}