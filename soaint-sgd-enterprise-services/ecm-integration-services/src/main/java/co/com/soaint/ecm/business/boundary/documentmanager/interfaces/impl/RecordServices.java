package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.uti.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
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
    private String idPadre = "";
    String idSubCategoria = "";
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

    private String codigoOrgAUX = " ";
    private String codCategory = "";
    private Map<String, String> codigosRecord = new HashMap<>();
    private Map<String, String> propiedades = new HashMap<>();
    private Map<String, String> codigosSubseries = new HashMap<>();
    @Autowired
    ContentControlAlfresco conexionCMIS;


    @Override
    public MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException {
        Map<String, String> idNodosPadre = new HashMap<>();
        codigosRecord = new HashMap<>();
        for (EstructuraTrdDTO estructura : structure) {
            Utilities utils = new Utilities ( );
            utils.ordenarListaOrganigrama (estructura.getOrganigramaItemList ( ));
            List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
            List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();
            generarOrganigrama(organigramaList, idNodosPadre);
            generarDependencia(trdList);

        }
        idPadre = "";

        return MensajeRespuesta.newInstance()
                .codMensaje("OKOKOK")
                .build();
    }

    @Override
    public MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException {
        log.info("iniciar - Crear carpeta record: {}", entrada);
        Map<String, String> query = new HashMap<>();
        JSONObject parametro = new JSONObject();
        query.put("query","select * from cmis:folder where cmis:name like '100100.10000.345%'");
        query.put("language","cmis");
        parametro.put("query", query);

       String codigoNodo = buscarRuta(parametro);

        return MensajeRespuesta.newInstance()
                .codMensaje("OKOKOK")
                .build();
    }

    public String buscarRuta(JSONObject entrada) throws SystemException {
        log.info("iniciar - buscar ruta: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SEARCH_ALFRESCO));
            Response response = wt.path("")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header("Content-Type", "application/json")
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)));
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


    public String crearRootCategory(JSONObject entrada) throws SystemException {
        log.info("iniciar - Crear categoria padre: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/file-plans/" + "96acce1c-fd76-491c-8177-75fd7c94c112" + "/categories")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header("Content-Type", "application/json")
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

        String nombreSerie =  trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat("_").concat(trd.getNomSerie());
        serie.put("name", nombreSerie);
        serie.put(tipoNodo, recordCategoria);
        propiedades.put("rmc:xSerie",nombreSerie);
        propiedades.put("rmc:xCodSerie", trd.getCodSerie());
        serie.put(tagPropiedades, propiedades);
        return crearNodo(serie, idSubCategoria);

    }

    private void crearSubserie(ContenidoDependenciaTrdDTO trd) throws SystemException {

        if ((!codigosSubseries.containsKey(trd.getCodSubSerie()) || !codigosSubseries.get(trd.getCodSubSerie()).equalsIgnoreCase(trd.getNomSubSerie())) && !trd.getCodSubSerie().equals("")) {

            String nombreSubserie =  trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat(".").concat(trd.getCodSubSerie()).concat("_").concat(trd.getNomSubSerie());
            JSONObject subSerie = new JSONObject();
            subSerie.put("name", nombreSubserie);
            subSerie.put("nodeType", recordCategoria);
            propiedades.put("rmc:xSubserie",nombreSubserie);
            propiedades.put("rmc:xCodSubSerie", trd.getCodSubSerie());
            subSerie.put(tagPropiedades, propiedades);
            crearNodo(subSerie, codCategory);
            codigosSubseries.put(trd.getCodSubSerie(), trd.getNomSubSerie());
        }


    }

    private String obtenerIdRuta(JSONObject respuestaJson) {
        String codigoId = "";
        JSONArray listaJson = respuestaJson.getJSONArray("list");
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

}
