package co.com.soaint.correspondencia.integration.service.rs;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/")
public class GestionarConstantesRS {
    @Autowired
    private GestionarConstantes boundary;

    public GestionarConstantesRS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("constantes/{codigo}")
    public List<TvsConstantes> listarConstantesByCodigoAndEstado(@PathParam("codigo") String codigo) throws BusinessException, SystemException{
        return boundary.listarConstantesByCodigoAndEstado(codigo);
    }
}
