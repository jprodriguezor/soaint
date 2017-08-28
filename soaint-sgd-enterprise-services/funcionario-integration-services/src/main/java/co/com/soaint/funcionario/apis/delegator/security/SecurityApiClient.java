package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.apis.delegator.security.wsclient.SecurityAPIService;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class SecurityApiClient {

    @Value("${securityapi.endpoint.url}")
    private String endpoint = "";

    public List<String> listarUsusriosByRol(String rol)throws SystemException{
        List<String> usuarios = new ArrayList<>();
        try {
            SecurityAPIService securityApiService = new SecurityAPIService(new URL(endpoint));
            securityApiService.getSecurityAPIPort().obtenerUsuariosporRol(rol).getUsuarios().getUsuario()
                    .stream().forEach(usuario -> usuarios.add(usuario.getUsername()));
            return usuarios;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
    public static void main(String[] args){
        try {
            URL url = new URL("http://192.168.3.242:28080/Security-Cartridge/SecurityAPI?wsdl");
            SecurityAPIService securityApiService = new SecurityAPIService(url);
            System.out.print(securityApiService.getSecurityAPIPort().listadoDeRoles());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
