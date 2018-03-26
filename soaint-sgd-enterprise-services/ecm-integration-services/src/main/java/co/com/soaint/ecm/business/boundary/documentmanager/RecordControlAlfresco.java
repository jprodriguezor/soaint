package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.RecordControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

@BusinessControl
public class RecordControlAlfresco implements RecordControl {

    private static final Logger logger = LogManager.getLogger(RecordControlAlfresco.class.getName());

    private final RecordServices recordServices;

    @Autowired
    public RecordControlAlfresco(RecordServices recordServices) {
        this.recordServices = recordServices;
    }

    /**
     * Permite cerrar una unidad documental
     *
     * @param unidadDocumentalDTO Objeto Unidad documental a cerrar
     * @return boolean Indica si se ha cerrado con exito
     * @throws SystemException SystemException
     */
    @Override
    public boolean cerrarUnidadDocumentalRecord(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {

        EntradaRecordDTO entradaRecordDTO = new EntradaRecordDTO();
        entradaRecordDTO.setSede(unidadDocumentalDTO.getCodigoSede());
        entradaRecordDTO.setDependencia(unidadDocumentalDTO.getCodigoDependencia());
        entradaRecordDTO.setSerie(unidadDocumentalDTO.getCodigoSerie());
        entradaRecordDTO.setSubSerie(unidadDocumentalDTO.getCodigoSubSerie());
        entradaRecordDTO.setNombreCarpeta(unidadDocumentalDTO.getNombreUnidadDocumental());

        //Se crea la unidad documental en el record
        final String idRecordFolder = recordServices.crearCarpetaRecord(entradaRecordDTO);

        boolean result = false;
        if (!ObjectUtils.isEmpty(idRecordFolder)) {

            if (!unidadDocumentalDTO.getListaDocumentos().isEmpty()) {

                for (DocumentoDTO documentoDTO : unidadDocumentalDTO.getListaDocumentos()) {

                    final String idDocumento = documentoDTO.getIdDocumento();
                    final String nombreDocumento = documentoDTO.getNombreDocumento();

                    //Se declara el record
                    if (!ObjectUtils.isEmpty(recordServices.declararRecord(idDocumento))) {
                        logger.info("Se declara el documento '{}' como record", nombreDocumento);
                    }
                    //Se completa el record
                    if (!ObjectUtils.isEmpty(recordServices.completeRecord(idDocumento))) {
                        logger.info("Se completa el record para el documento '{}' como record", nombreDocumento);
                    }
                    //Se archiva el record
                    if (!ObjectUtils.isEmpty(recordServices.fileRecord(idDocumento, idRecordFolder))) {
                        logger.info("Se archiva el documento '{}'", nombreDocumento);
                    }
                }
            }
            //Se cierra la carpeta
            result = recordServices.abrirCerrarRecordFolder(idRecordFolder,true);
        }
        return result;
    }
}
