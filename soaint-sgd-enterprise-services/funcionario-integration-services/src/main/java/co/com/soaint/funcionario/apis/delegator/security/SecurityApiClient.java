package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.funcionario.apis.delegator.security.wsclient.SecurityAPIService;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.net.URL;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class SecurityApiClient {
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
