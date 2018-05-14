package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreSerie")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/serie/1.0.0")
public class SerieDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String codigoSerie;
    private String nombreSerie;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public SerieDTO(String codigoBase, String nombreBase, String codigoSerie, String nombreSerie) {
        super(codigoBase, nombreBase);
        this.codigoSerie = codigoSerie;
        this.nombreSerie = nombreSerie;
    }
}
