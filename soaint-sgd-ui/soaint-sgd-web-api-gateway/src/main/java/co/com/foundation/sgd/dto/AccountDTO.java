package co.com.foundation.sgd.dto;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.PrincipalContext;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Class for Represenet User login information
 */
@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class AccountDTO {

    private String token;

    private FuncionarioDTO profile;
}