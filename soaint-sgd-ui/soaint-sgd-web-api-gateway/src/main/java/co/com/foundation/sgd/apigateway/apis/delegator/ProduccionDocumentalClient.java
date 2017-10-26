package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ApiDelegator
@Log4j2
public class ProduccionDocumentalClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

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

        for (Map proyector: (ArrayList<Map>)entrada.getParametros().get("proyectores")) {
            nuevaEntrada.getParametros().clear();
            LinkedHashMap funcionario = (LinkedHashMap)proyector.get("funcionario");
            nuevaEntrada.getParametros().put("usuarioProyector", funcionario.getOrDefault("loginName","").toString());
            log.info("\n\r== Nueva entrada: "+nuevaEntrada.toString()+" ==\n\r");
            procesoClient.iniciar(nuevaEntrada);
        }

        return procesoClient.listarPorIdProceso(entrada);
    }

}
