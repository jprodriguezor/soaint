package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nombreSubSerie")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/sub-serie/1.0.0")
public class SubSerieDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String codigoSubSerie;
    private String nombreSubSerie;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public SubSerieDTO(String codigoBase, String nombreBase, String codigoSubSerie, String nombreSubSerie) {
        super(codigoBase, nombreBase);
        this.codigoSubSerie = codigoSubSerie;
        this.nombreSubSerie = nombreSubSerie;
    }
}
