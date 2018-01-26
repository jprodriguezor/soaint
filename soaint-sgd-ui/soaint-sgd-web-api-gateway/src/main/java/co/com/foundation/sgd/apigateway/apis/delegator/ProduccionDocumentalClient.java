package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@ApiDelegator
@Log4j2
public class ProduccionDocumentalClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);

    @Autowired
    private ProcesoClient procesoClient;

    public ProduccionDocumentalClient() {
        super();
    }


    public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {
        log.info("\n\r== Produccion Documental - [trafic] - ejecutar proyector:  ==\n\r");

        EntradaProcesoDTO nuevaEntrada = new EntradaProcesoDTO();
        nuevaEntrada.setIdDespliegue(entrada.getParametros().get("idDespliegue").toString());
        nuevaEntrada.setIdProceso(entrada.getParametros().get("idProceso").toString());
        nuevaEntrada.setUsuario(entrada.getUsuario());
        nuevaEntrada.setPass(entrada.getPass());
        nuevaEntrada.setParametros(new HashMap<>());

        String numeroRadicado = entrada.getParametros().getOrDefault("numeroRadicado", "").toString();
        String fechaRadicacion = entrada.getParametros().getOrDefault("fechaRadicacion", "").toString();

        for (Map proyector : (ArrayList<Map>) entrada.getParametros().get("proyectores")) {
            nuevaEntrada.getParametros().clear();
            LinkedHashMap funcionario = (LinkedHashMap) proyector.get("funcionario");
            LinkedHashMap sedeAdministrativa = (LinkedHashMap) proyector.get("sede");
            LinkedHashMap dependencia = (LinkedHashMap) proyector.get("dependencia");
            LinkedHashMap tipoPlantilla = (LinkedHashMap) proyector.get("tipoPlantilla");
            nuevaEntrada.getParametros().putAll(new HashMap<String, Object>() {
                {
                    put("usuarioProyector", funcionario.getOrDefault("loginName", ""));
                    put("numeroRadicado", numeroRadicado);
                    put("fechaRadicacion", fechaRadicacion);
                    put("codigoSede", sedeAdministrativa.getOrDefault("codigo", null));
                    put("codDependencia", dependencia.getOrDefault("codigo", null));
                    put("codigoTipoPlantilla", tipoPlantilla.getOrDefault("codigo", null));
                }
            });
            log.info("\n\r== Nueva entrada: " + nuevaEntrada.toString() + " ==\n\r");
            procesoClient.iniciar(nuevaEntrada);
        }

        return procesoClient.listarPorIdProceso(entrada);
    }

    public Response producirDocumento(String sede, String dependencia, String fileName, InputPart part, String parentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();
        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }
        multipart.addFormData("documento", inputStream, MediaType.MULTIPART_FORM_DATA_TYPE);
        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {};

        return wt.path("/subirDocumentoRelacionECM/" + sede + "/" + dependencia + "/" + fileName +
                (parentId == null || "".equals(parentId) ? "" : "/" + parentId ))
                .request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

}
