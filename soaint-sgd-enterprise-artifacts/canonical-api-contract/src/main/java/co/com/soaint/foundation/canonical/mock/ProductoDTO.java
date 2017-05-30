package co.com.soaint.foundation.canonical.mock;

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
public class ProductoDTO {

	private int id;
	private String nombre;
	private long costo;

	public ProductoDTO() {
		super();
	}

}
