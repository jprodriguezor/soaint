package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreSede")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/sede/1.0.0")
public class SedeDTO extends BaseDTO {

    private static final Long serialVersionUID = 15L;

    private String codigoSede;
    private String nombreSede;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public SedeDTO(String codigoBase, String nombreBase, String codigoSede, String nombreSede) {
        super(codigoBase, nombreBase);
        this.codigoSede = codigoSede;
        this.nombreSede = nombreSede;
    }
}
