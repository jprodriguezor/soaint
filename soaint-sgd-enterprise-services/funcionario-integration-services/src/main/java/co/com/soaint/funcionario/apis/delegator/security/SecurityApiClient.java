package co.com.soaint.funcionario.apis.delegator.security;

import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import com.soaint.services.security_cartridge._1_0.SecurityAPIService;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.net.URL;

/**
 * Created by esanchez on 8/28/2017.
 */
//@ApiDelegator
//@Log4j2
//@NoArgsConstructor
public class SecurityApiClient {
    public static void main(String[] args){

        try {
            URL url = new URL("http://192.168.3.242:28080/Security-Cartridge/SecurityAPI?wsdl");
            SecurityAPIService securityApiService = new SecurityAPIService(url);
            System.out.print("Autenticacion: " + securityApiService.getSecurityAPIPort().verifyCredentials("jorge.infante","descarga*22").isSuccessful());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
