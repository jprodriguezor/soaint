package co.com.soaint.bpm.services.integration.services;

import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PaisesDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Arce on 6/7/2017.
 */

@Path("/bpm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BpmIntegrationServicesClientRest {

    private static Logger LOGGER = LogManager.getLogger(BpmIntegrationServicesClientRest.class.getName());

    @Autowired
    private IProcessServices proceso;

    public BpmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    @POST
    @Path("/proceso/iniciar/")
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso)throws SystemException, BusinessException {
        LOGGER.info("processing rest request - iniciar proceso");
        try {
            return proceso.inicarProceso(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }




    }

//    public EntradaProcesoDTO iniciarProceso()throws SystemException, BusinessException {
//        LOGGER.info("processing rest request - iniciar proceso");
//        EntradaProcesoDTO entrada = new EntradaProcesoDTO();
//
//        entrada.setIdDespliegue("");
//        entrada.setIdProceso("");
//        entrada.setPass("");
//        entrada.setUsuario("");
//        Map<String, Object> params2 = new HashMap<>();
//        params2.put("cuenta", "krisv");
//        params2.put("nombre", "Yearly performance evaluation");
//        entrada.setParametros(params2);
//
//        return entrada;
//    }


}
