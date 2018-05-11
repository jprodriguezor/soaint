package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by amartinez on 24/01/2018.
 */
@Service
public interface IRecordServices {

    int AUTO_CLOSE_NUM_DAYS = 8;

    String RMA_IS_CLOSED = "rma:isClosed";

    /**
     * Permite crear la estructura en el record
     * @param structure objeto que contiene la estructura
     * @return respuesta satisfactoria para la creacion del estructura
     * @throws SystemException
     */
    MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException;

    /**
     * permite crear carpeta en el record
     * @param entrada objeto que contiene la informacion necesaria para crear la carpeta
     * @return respuesta satisfactoria para la creacion de la carpeta
     * @throws SystemException
     */
    MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException;

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param unidadDocumentalDTO  Obj Unidad Documental
     * @return MensajeRespuesta
     */
    MensajeRespuesta gestionarUnidadDocumentalECM(final UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    /**
     * Metodo para abrir/cerrar una unidad documental
     *
     * @param unidadDocumentalDTOS Lista de Unidad Documental
     * @return MensajeRespuesta
     */
    MensajeRespuesta gestionarUnidadesDocumentalesECM(final List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;

    /**
     * Metodo para Obtener un recordFolder
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return Folder folder
     */
    Optional<Folder> obtenerRecordFolder(String idUnidadDocumental) throws SystemException;
}
