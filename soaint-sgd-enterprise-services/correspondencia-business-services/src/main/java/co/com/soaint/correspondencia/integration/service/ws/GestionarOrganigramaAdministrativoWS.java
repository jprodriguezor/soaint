package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by esanchez on 6/22/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarOrganigramaAdministrativoWS {

    @Autowired
    GestionarOrganigramaAdministrativo boundary;

    public GestionarOrganigramaAdministrativoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarPaisesByEstado", operationName = "listarPaisesByEstado")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException{
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }
}
