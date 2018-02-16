package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.uti.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by amartinez on 24/01/2018.
 */
@BusinessControl
@NoArgsConstructor
@Log4j2
public class RecordServices implements IRecordServices {
    String idSubCategoria = "";
    @Autowired
    ContentControlAlfresco conexionCMIS;
    private String idPadre = "";
    @Value("${protocolo}")
    private String protocolo = "";
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${tokken.record}")
    private String encoding = "";
    @Value("${nodeType}")
    private String tipoNodo = "";
    @Value("${recordCategory}")
    private String recordCategoria = "";
    @Value("${tag.propiedades}")
    private String tagPropiedades = "";
    @Value("${recordCarpeta}")
    private String recordCarpeta = "";
    @Value("${id.sitio.record.manager}")
    private String idRecordManager = "";
    private String codigoOrgAUX = " ";
    private String codCategory = "";
    private Map<String, String> codigosRecord = new HashMap<>();
    private Map<String, String> propiedades = new HashMap<>();
    private Map<String, String> codigosSubseries = new HashMap<>();

    @Override
    public MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException {
        log.info("iniciar - Crear estructura en record: {}");
        try {
        Map<String, String> idNodosPadre = new HashMap<>();
        codigosRecord = new HashMap<>();
        for (EstructuraTrdDTO estructura : structure) {
            Utilities utils = new Utilities();
            utils.ordenarListaOrganigrama(estructura.getOrganigramaItemList());
            List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
            List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();
            generarOrganigrama(organigramaList, idNodosPadre);
            generarDependencia(trdList);

        }
        idPadre = "";

        return MensajeRespuesta.newInstance()
                .codMensaje("0000")
                .mensaje("Estructura creada correctamente")
                .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin -  Crear estructura en record: ");
        }
    }

    @Override
    public MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException {
        log.info("iniciar - Crear carpeta record: {}", entrada);
        try {
        Map<String, String> query = new HashMap<>();
        JSONObject parametro = new JSONObject();
        String queryPrincipal = "select * from rmc:rmarecordCategoryCustomProperties where rmc:xSeccion = '"+ entrada.getDependencia() +"' and  rmc:xCodSerie = '" + entrada.getSerie() + "' ";
        if(!entrada.getSubSerie().equals("")){
            String condicionSubserie = " and  rmc:xCodSubSerie = '"+entrada.getSubSerie()+"' ";
           queryPrincipal = queryPrincipal.concat(condicionSubserie);
        }
       String codigoBusqueda = entrada.getDependencia().concat(".").concat(entrada.getSerie()).concat(".").concat(entrada.getSubSerie());
        query.put("query", queryPrincipal);
        query.put("language", "cmis");
        parametro.put("query", query);
        JSONObject carpeta = new JSONObject();
        carpeta.put("name", entrada.getNombreCarpeta());
        carpeta.put(tipoNodo, recordCarpeta);
        crearNodo(carpeta, buscarRuta(parametro));


        return MensajeRespuesta.newInstance()
                .codMensaje("0000")
                .mensaje("Carpeta creda en ".concat(codigoBusqueda))
                .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin -  Crear carpeta record: ");
        }
    }

    public String buscarRuta(JSONObject entrada) throws SystemException {
        log.info("iniciar - buscar ruta: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SEARCH_ALFRESCO));
            Response response = wt.path("")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "nodeType", "rma:recordCategory");
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - buscar ruta ");
        }

    }

    public String obtenerIdFilePlan() throws SystemException {
        log.info("iniciar - obtener id file plan: {}");
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_CORE_ALFRESCO));
            Response response = wt.path("/sites/" + idRecordManager + "/containers")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .get();

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "folderId", "documentLibrary");
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - obtener id file plan ");
        }

    }


    public String crearRootCategory(JSONObject entrada) throws SystemException {
        log.info("iniciar - Crear categoria padre: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/file-plans/" + obtenerIdFilePlan() + "/categories")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria padre ");
        }

    }

    public String crearNodo(JSONObject entrada, String idPadre) throws SystemException {
        log.info("iniciar - Crear categoria hija: {}", entrada.toString());
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/record-categories/" + idPadre + "/children")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));
            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    private String obtenerIdPadre(JSONObject respuestaJson) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("entry".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                codigoId = valor.getString("id");
            }
        }
        return codigoId;
    }

    private void generarOrganigrama(List<OrganigramaDTO> organigramaList, Map<String, String> idNodosPadre) throws SystemException {
        propiedades = new HashMap<>();

        for (OrganigramaDTO organigrama : organigramaList) {

            String codigoOrg = organigrama.getCodOrg();
            JSONObject entrada = new JSONObject();
            entrada.put("name", organigrama.getNomOrg());
            entrada.put(tipoNodo, recordCategoria);
            String idOrganAdmin = String.valueOf(organigrama.getIdeOrgaAdmin());
            if ("P".equalsIgnoreCase(organigrama.getTipo()) && !idNodosPadre.containsKey(idOrganAdmin)) {
                propiedades.put("rmc:xFondo", organigrama.getCodOrg());
                entrada.put(tagPropiedades, propiedades);
                idPadre = crearRootCategory(entrada);
                codigosRecord.put(codigoOrg, idPadre);
                idNodosPadre.put(idOrganAdmin, idPadre);
                codigoOrgAUX = organigrama.getCodOrg();

            } else {
                if (!codigosRecord.containsKey(codigoOrg)) {
                    propiedades.put("rmc:xSeccion", organigrama.getCodOrg());
                    entrada.put(tagPropiedades, propiedades);
                    idSubCategoria = crearNodo(entrada, codigosRecord.get(codigoOrgAUX));
                    codigoOrgAUX = organigrama.getCodOrg();
                    codigosRecord.put(organigrama.getCodOrg(), idSubCategoria);
                    idNodosPadre.put(idOrganAdmin, idPadre);
                } else {
                    codigoOrgAUX = organigrama.getCodOrg();
                }
            }
        }
    }

    private void generarDependencia(List<ContenidoDependenciaTrdDTO> trdList) throws SystemException {
        codigosSubseries = new HashMap<>();
        Map<String, String> codigoSeries = new HashMap<>();
        String codigoSerieAUX = "";
        for (ContenidoDependenciaTrdDTO trd : trdList) {
            JSONObject serie = new JSONObject();


            if (codigoOrgAUX.equalsIgnoreCase(trd.getIdOrgOfc()) && (trd.getCodSubSerie() == null || trd.getCodSubSerie().equals("")) && !codigoSeries.containsKey(trd.getIdOrgOfc())) {
                crearSerie(trd, serie);
                codigoSeries.put(trd.getIdOrgOfc(), idPadre);
            } else {
                if (codigoSerieAUX.equals("") || !codigoSerieAUX.equals(trd.getCodSerie())) {

                    codCategory = crearSerie(trd, serie);
                    codigoSerieAUX = trd.getCodSerie();
                    codigoSeries.put(trd.getIdOrgOfc(), codCategory);
                }

                crearSubserie(trd);
            }
        }
    }

    private String crearSerie(ContenidoDependenciaTrdDTO trd, JSONObject serie) throws SystemException {

        String nombreSerie = trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat("_").concat(trd.getNomSerie());
        serie.put("name", nombreSerie);
        serie.put(tipoNodo, recordCategoria);
        propiedades.put("rmc:xSerie", nombreSerie);
        propiedades.put("rmc:xCodSerie", trd.getCodSerie());
        serie.put(tagPropiedades, propiedades);
        return crearNodo(serie, idSubCategoria);

    }

    private void crearSubserie(ContenidoDependenciaTrdDTO trd) throws SystemException {

        if ((!codigosSubseries.containsKey(trd.getCodSubSerie()) || !codigosSubseries.get(trd.getCodSubSerie()).equalsIgnoreCase(trd.getNomSubSerie())) && !trd.getCodSubSerie().equals("")) {

            String nombreSubserie = trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat(".").concat(trd.getCodSubSerie()).concat("_").concat(trd.getNomSubSerie());
            JSONObject subSerie = new JSONObject();
            subSerie.put("name", nombreSubserie);
            subSerie.put(tipoNodo, recordCategoria);
            propiedades.put("rmc:xSubserie", nombreSubserie);
            propiedades.put("rmc:xCodSubSerie", trd.getCodSubSerie());
            subSerie.put(tagPropiedades, propiedades);
            crearNodo(subSerie, codCategory);
            codigosSubseries.put(trd.getCodSubSerie(), trd.getNomSubSerie());
        }


    }

    private String obtenerIdRuta(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("list".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                JSONArray listaNodosJson = valor.getJSONArray("entries");
                for (int i = 0; i < listaNodosJson.length(); i++) {
                    JSONObject valorJson = (JSONObject) listaNodosJson.get(i);
                    codigoId = obtenerIdNodo(valorJson,nodo,nombreNodo);

                }

            }
        }
        return codigoId;
    }

    private String obtenerIdNodo(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String nodoId = "";
        Iterator keys1 = respuestaJson.keys();
        while (keys1.hasNext()) {
            Object key1 = keys1.next();
            if ("entry".equalsIgnoreCase(key1.toString())) {
                JSONObject valor1 = respuestaJson.getJSONObject((String) key1);
                if (valor1.getString(nodo).equalsIgnoreCase(nombreNodo))
                    nodoId = valor1.getString("id");
            }
        }
        return nodoId;
    }

}