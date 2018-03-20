package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@BusinessControl
@NoArgsConstructor
public class RecordControlAlfresco {

    @Autowired
    RecordServices recordServices;

    public MensajeRespuesta cerrarUnidadDocumentalRecord(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            if (!ObjectUtils.isEmpty(unidadDocumentalDTO.getId())) {
                EntradaRecordDTO entradaRecordDTO = new EntradaRecordDTO();
                entradaRecordDTO.setSede(unidadDocumentalDTO.getCodigoSede());
                entradaRecordDTO.setDependencia(unidadDocumentalDTO.getCodigoDependencia());
                entradaRecordDTO.setSerie(unidadDocumentalDTO.getCodigoSerie());
                entradaRecordDTO.setSubSerie(unidadDocumentalDTO.getCodigoSubSerie());
                entradaRecordDTO.setNombreCarpeta(unidadDocumentalDTO.getNombreUnidadDocumental());
                //Se crea la unidad documental en el record
                MensajeRespuesta mensajeRespuestaAux = recordServices.crearCarpetaRecord(entradaRecordDTO);

                if (mensajeRespuestaAux.getCodMensaje().equals("0000")) {
                    if (!unidadDocumentalDTO.getListaDocumentos().isEmpty()) {
                        for (DocumentoDTO documentoDTO : unidadDocumentalDTO.getListaDocumentos()) {
                            //Se declara el record
                            recordServices.declararRecord(documentoDTO.getIdDocumento());
                            //Se completa el record
                            recordServices.completeRecord(documentoDTO.getIdDocumento());
                            //Se archiva el record
                            recordServices.fileRecord(documentoDTO.getIdDocumento(), mensajeRespuestaAux.getResponse().get("idUnidadDocumental").toString());
                        }
                    }
                    //Se cierra la carpeta
                    Map<String, Object> response = mensajeRespuestaAux.getResponse();

                    String idRecordFolder = unidadDocumentalDTO.getId();
                    if (response.containsKey("idUnidadDocumental")) {
                        idRecordFolder = (String) response.get("idUnidadDocumental");
                    }
                    recordServices.abrirCerrarRecordFolder(idRecordFolder,true);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw e;
        }
        return respuesta;
    }
}
