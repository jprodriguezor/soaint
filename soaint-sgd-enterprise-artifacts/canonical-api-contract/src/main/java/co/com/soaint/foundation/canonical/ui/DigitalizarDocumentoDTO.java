package co.com.soaint.foundation.canonical.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data()
@ToString
@XmlRootElement
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class DigitalizarDocumentoDTO {

    private List<String> ecmIds;
}
