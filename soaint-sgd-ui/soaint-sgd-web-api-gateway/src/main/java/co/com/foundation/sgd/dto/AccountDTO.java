package co.com.foundation.sgd.dto;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.PrincipalContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class AccountDTO {

    private String token;

    private PrincipalContext profile;
}