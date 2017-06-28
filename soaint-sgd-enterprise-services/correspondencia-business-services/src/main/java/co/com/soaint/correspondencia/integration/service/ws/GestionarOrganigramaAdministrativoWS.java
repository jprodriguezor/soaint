package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;
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

    @WebMethod(action = "consultarOrganigrama", operationName = "consultarOrganigrama")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException{
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }

    @WebMethod(action = "listarDescendientesDirectosDeElementoRayz", operationName = "listarDescendientesDirectosDeElementoRayz")
    public OrganigramaAdministrativoDTO listarDescendientesDirectosDeElementoRayz() throws BusinessException, SystemException{
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarDescendientesDirectosDeElementoRayz()).build();
    }

    @WebMethod(action = "listarElementosDeNivelInferior", operationName = "listarElementosDeNivelInferior")
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(@WebParam(name = "id_padre")final String idPadre) throws BusinessException, SystemException{
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(BigInteger.valueOf(Long.parseLong(idPadre)))).build();
    }

    @WebMethod(action = "consultarPadreDeSegundoNivel", operationName = "consultarPadreDeSegundoNivel")
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(@WebParam(name = "id_hijo")final String idHijo) throws BusinessException, SystemException{
        return boundary.consultarPadreDeSegundoNivel(BigInteger.valueOf(Long.parseLong(idHijo)));
    }
}
