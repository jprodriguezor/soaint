package co.com.soaint.correspondencia.integration.service.rs;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.correspondencia.domain.entity.TvsMunicipio;
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
public class GestionarMunicipioRS {

    @Autowired
    private GestionarMunicipio boundary;

    public GestionarMunicipioRS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("municipio/{codDepar}")
    public List<TvsMunicipio> listarMunicipiosByCodDepar(@PathParam("codDepar") String codDepar)throws BusinessException, SystemException{
        return boundary.listarMunicipiosByCodDepar(codDepar);
    }
}
