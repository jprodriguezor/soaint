package co.com.soaint.foundation.canonical.seguridad;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName="newBuilder")
public class UsuarioDTO {

	private String login;
	private String password;
	private String domain;

	public UsuarioDTO() {
		super();
	}
	
}
