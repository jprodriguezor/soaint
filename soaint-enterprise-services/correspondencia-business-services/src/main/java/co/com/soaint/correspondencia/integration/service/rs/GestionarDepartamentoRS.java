package co.com.soaint.correspondencia.integration.service.rs;

import co.com.soaint.correspondencia.business.boundary.GestionarDepartamento;
import co.com.soaint.correspondencia.domain.entity.TvsDepartamento;
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
public class GestionarDepartamentoRS {

    @Autowired
    private GestionarDepartamento boundary;

    public GestionarDepartamentoRS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("departamento/{codPais}")
    public List<TvsDepartamento> listarDepartamentosByCodPais(@PathParam("codPais") String codPais) throws BusinessException, SystemException{
        return boundary.listarDepartamentosByCodPais(codPais);
    }
}
