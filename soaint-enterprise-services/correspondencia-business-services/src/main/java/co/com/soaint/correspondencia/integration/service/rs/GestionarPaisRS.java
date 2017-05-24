package co.com.soaint.correspondencia.integration.service.rs;

import co.com.soaint.correspondencia.business.boundary.GestionarPais;
import co.com.soaint.correspondencia.domain.entity.TvsPais;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/")
public class GestionarPaisRS {

    @Autowired
    private GestionarPais boundary;

    public GestionarPaisRS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("pais/")
    public List<TvsPais> listarPaisesByEstado() throws BusinessException, SystemException {
        List<TvsPais> paises = boundary.listarPaisesByEstado();
        return paises;
    }
}
