package co.com.soaint.foundation.canonical.mock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName="newBuilder")
public class ListaSeleccionSimpleDTO {

	private int id;
	private String nombre;

	public ListaSeleccionSimpleDTO() {
		super();
	}

}
