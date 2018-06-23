package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.*;

@ApiDelegator
@Log4j2
public class ProduccionDocumentalClient {

    private String ecm_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);
    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //Client client = ClientBuilder.newClient();

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

        final String numeroRadicado = entrada.getParametros().getOrDefault("numeroRadicado", "").toString();
        final String fechaRadicacion = entrada.getParametros().getOrDefault("fechaRadicacion", "").toString();

        final List proyectores = (ArrayList) entrada.getParametros().get("proyectores");
        for (Object obj :
                proyectores) {
            nuevaEntrada.getParametros().clear();
            final Map proyector = (Map) obj;
            final LinkedHashMap funcionario = (LinkedHashMap) proyector.get("funcionario");
            final LinkedHashMap sedeAdministrativa = (LinkedHashMap) proyector.get("sede");
            final LinkedHashMap dependencia = (LinkedHashMap) proyector.get("dependencia");
            final LinkedHashMap tipoPlantilla = (LinkedHashMap) proyector.get("tipoPlantilla");

            final Map<String, Object> parameters = new HashMap<>();
            final String loginName = funcionario.containsKey("loginName") ? funcionario.get("loginName") + "" : "";
            parameters.put("usuarioProyector", loginName);
            parameters.put("numeroRadicado", numeroRadicado);
            parameters.put("fechaRadicacion", fechaRadicacion);
            final String codigoSede = sedeAdministrativa.containsKey("codigo") ? sedeAdministrativa.get("codigo") + "" : null;
            parameters.put("codigoSede", codigoSede);
            final String codDependencia = dependencia.containsKey("codigo") ? dependencia.get("codigo") + "" : null;
            parameters.put("codDependencia", codDependencia);
            final Object codigoTipoPlantilla = tipoPlantilla.containsKey("codigo") ? tipoPlantilla.get("codigo") : null;
            parameters.put("codigoTipoPlantilla", codigoTipoPlantilla);
            nuevaEntrada.getParametros().putAll(parameters);
            log.info("\n\r== Nueva entrada: " + nuevaEntrada.toString() + " ==\n\r");
            procesoClient.iniciar(nuevaEntrada);
        }
        return procesoClient.listarPorIdProceso(entrada);
    }

    public Response obtenerDatosDocumentoPorNroRadicado(String nro) {
        log.info("ProduccionDocumental - [trafic] - Obtener datos del documento por Nro Radicado: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/" .concat(nro))
                .request()
                .get();
    }

}
