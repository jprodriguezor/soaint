package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Map;

@ApiDelegator
@Log4j2
public class ProduccionDocumentalClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public ProduccionDocumentalClient() {
        super();
    }


    public void ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {
        log.info("Produccion Documental - [trafic] - ejecutar proyector: " + endpoint);
        ArrayList<Map> proyectores = (ArrayList<Map>)((Map)entrada.getParametros()).get("proyectores");
        for (Map proyector: proyectores) {
            log.info(proyector);
        }
        log.info("PROYECTOR ENDED");
    }

}
