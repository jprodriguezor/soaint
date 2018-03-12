package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@BusinessControl
@NoArgsConstructor
public class RecordControlAlfresco {
    @Autowired
    RecordServices recordServices;

    public MensajeRespuesta cerrarUnidadDocumentalRecord(MensajeRespuesta mensajeRespuesta){
        MensajeRespuesta respuesta= new MensajeRespuesta();
        try {
            if("0000".equals(mensajeRespuesta.getCodMensaje())){
                if
            }
        }catch (Exception e){

        }
        return respuesta;
    }
}
